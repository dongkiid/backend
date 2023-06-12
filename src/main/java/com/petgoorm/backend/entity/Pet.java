package com.petgoorm.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="pet")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member")
public class Pet extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "petname")
    private String petname;

    @Column(name = "age")
    private Integer age;


    @Column(name = "pet_url")
    private String petUrl;

    @Column(name = "firstmet")
    private Date firstmet;

    @Column(name="birth")
    private Date birth;

    @Column(name = "type")
    private String type;

    @Column(name = "weight")
    private Float weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name="member_id")
    private Member member;

}