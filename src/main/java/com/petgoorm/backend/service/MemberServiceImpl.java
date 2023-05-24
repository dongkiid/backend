package com.petgoorm.backend.service;

import com.petgoorm.backend.dto.ResponseDTO;
import com.petgoorm.backend.dto.member.MemberRequestDTO;
import com.petgoorm.backend.dto.member.MemberResponseDTO;
import com.petgoorm.backend.entity.Member;
import com.petgoorm.backend.jwt.JwtTokenProvider;
import com.petgoorm.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;

    //회원등록
    @Override
    @Transactional
    public ResponseDTO<Long> signup(MemberRequestDTO.SignUp memberRequestDTO) {
        if (memberRepository.existsByEmail(memberRequestDTO.getEmail())) {
            return ResponseDTO.of(HttpStatus.CONFLICT.value(),"이미 가입되어 있는 유저입니다",null);
        }
        Member member = toEntity(passwordEncoder, memberRequestDTO);
        memberRepository.save(member);
        return ResponseDTO.of(HttpStatus.OK.value(),"회원가입이 성공했습니다.",member.getId());

    }

    //이메일 중복체크
    @Override
    @Transactional(readOnly = true)
    public ResponseDTO<String> checkEmailDuplication(String email){
        boolean emailDuplicate = memberRepository.existsByEmail(email);
        log.info("중복체크 이메일: " + emailDuplicate);
        try {
            if (emailDuplicate) {
                //중복
                return ResponseDTO.of(HttpStatus.CONFLICT.value(), "이미 가입되어 있는 유저입니다", null);
            } else {
                //사용가능한 이메일
                log.info("사용 가능한 이메일: " + email);
                return ResponseDTO.of(HttpStatus.OK.value(), "사용 가능한 이메일입니다.", email);
            }
        }catch (Exception e){
            return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 에러가 발생했습니다.", null);
        }
    }


    //닉네임 중복체크
    @Override
    @Transactional(readOnly = true)
    public ResponseDTO<String> checkNicknameDuplication(String nickname) {
        boolean nicknameDuplicate = memberRepository.existsByNickname(nickname);
        try {
            if (nicknameDuplicate) {
                //중복
                return ResponseDTO.of(HttpStatus.CONFLICT.value(), "이미 존재하는 닉네임입니다.", null);
            } else {
                //사용가능한 이메일
                return ResponseDTO.of(HttpStatus.OK.value(), "사용 가능한 닉네임입니다.", nickname);
            }
        }catch (Exception e){
            return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 에러가 발생했습니다.", null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO<MemberResponseDTO.TokenInfo> login(MemberRequestDTO.Login login) {

        //이메일이 db에 존재하지 않을때
        if (memberRepository.findByEmail(login.getEmail()).orElse(null) == null) {
            return ResponseDTO.of(HttpStatus.BAD_REQUEST.value(), "해당하는 유저가 존재하지 않습니다.", null);

        }

        try {
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
            UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
            log.info("1. authenticationToken : "+authenticationToken);
            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            log.info("2. authentication : "+authentication);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            MemberResponseDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            log.info("3. tokenInfo : "+tokenInfo);

            // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
            // getName = 사용자의 이메일
            redisTemplate.opsForValue()
                    .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);
            log.info("4. redis 저장");
            return ResponseDTO.of(HttpStatus.OK.value(), "로그인에 성공했습니다.", tokenInfo);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDTO.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 에러가 발생했습니다.", null);
        }

    }


}
