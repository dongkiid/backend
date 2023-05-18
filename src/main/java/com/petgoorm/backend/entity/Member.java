package com.petgoorm.backend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "provider")
    private String provider;

    @Column(name = "snsId")
    private String snsId;

    @Column(name = "member_pic")
    private String memberPic;

    @Column(name = "address")
    private String address;
}
