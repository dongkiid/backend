package com.petgoorm.backend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDTO {

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
