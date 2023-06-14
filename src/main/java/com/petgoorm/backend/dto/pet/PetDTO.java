package com.petgoorm.backend.dto.pet;

import com.petgoorm.backend.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetDTO {

    private Long petId;

    private String petname;

    private Integer age;

    private String petUrl;

    private Date firstmet;

    private Date birth;

    private String type;

    private Float weight;


}