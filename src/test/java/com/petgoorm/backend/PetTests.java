package com.petgoorm.backend;

import com.petgoorm.backend.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
public class PetTests {

    @Test
    public void nowEmail(){
        // 가상의 사용자 이메일 값
        String userEmail = "test@naver.com";

        // 가상의 인증 객체 생성
        Authentication authentication = new TestingAuthenticationToken(userEmail, null);

        // SecurityContextHolder에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);


        System.out.println(SecurityUtil.getCurrentUserEmail());
    }


}
