package com.petgoorm.backend.dto.member;

import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class MemberRequestDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUp {
        private String email;

        private String password;

        private String memberName;

        private String nickname;

        private String phoneNumber;

        private String provider;

        private String snsId;

        private String memberPic;

        private String address;

    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        private String email;
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

}
