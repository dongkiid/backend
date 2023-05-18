package com.petgoorm.backend.service;

import com.petgoorm.backend.dto.ResponseDTO;
import com.petgoorm.backend.dto.member.MemberRequestDTO;
import com.petgoorm.backend.entity.Member;
import com.petgoorm.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //회원등록
    @Override
    @Transactional
    public ResponseDTO<Long> signup(MemberRequestDTO memberRequestDTO) {
        if (memberRepository.existsByEmail(memberRequestDTO.getEmail())) {
            return ResponseDTO.of(HttpStatus.CONFLICT.value(),"이미 가입되어 있는 유저입니다",null);
        }
        Member member = toEntity(passwordEncoder, memberRequestDTO);
        memberRepository.save(member);
        return ResponseDTO.of(HttpStatus.OK.value(),"회원가입이 성공했습니다.",member.getMemberId());

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



}
