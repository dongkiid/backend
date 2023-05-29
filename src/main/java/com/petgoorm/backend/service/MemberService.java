package com.petgoorm.backend.service;


import com.petgoorm.backend.dto.ResponseDTO;
import com.petgoorm.backend.dto.member.MemberRequestDTO;
import com.petgoorm.backend.dto.member.MemberResponseDTO;
import com.petgoorm.backend.entity.Authority;
import com.petgoorm.backend.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

public interface MemberService {

    //DTO->Entity 변환 메서드
    default Member toEntity(PasswordEncoder passwordEncoder, MemberRequestDTO.SignUp requestDTO) {
        Member member = Member.builder()
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .memberName(requestDTO.getMemberName())
                .nickname(requestDTO.getNickname())
                .phoneNumber(requestDTO.getPhoneNumber())
                .provider("local")
                .snsId(requestDTO.getSnsId())
                .memberPic(requestDTO.getMemberPic())
                .address(requestDTO.getAddress())
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();
        return member;
    }

    //회원등록
    ResponseDTO<Long> signup(MemberRequestDTO.SignUp memberRequestDTO);

    //이메일 중복체크
    ResponseDTO<String> checkEmailDuplication(String email);

    //닉네임 중복체크
    ResponseDTO<String> checkNicknameDuplication(String nickname);

    ResponseDTO<MemberResponseDTO.TokenInfo> login(MemberRequestDTO.Login login);

    ResponseDTO<String> logout(String accessToken);
}
