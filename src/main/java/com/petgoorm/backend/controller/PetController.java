package com.petgoorm.backend.controller;

import com.petgoorm.backend.dto.ResponseDTO;
import com.petgoorm.backend.dto.pet.PetDTO;
import com.petgoorm.backend.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pet")
@RequiredArgsConstructor
@Log4j2
public class PetController {

    private final PetService petService;

    @PostMapping("/petform")
    public ResponseDTO<Long> petAdd(@RequestBody PetDTO petDTO){
        log.info(petDTO);
        return petService.petAdd(petDTO);

    }

    @GetMapping("/petinfo")
    public ResponseDTO<PetDTO> petInfoResponse(){
        return petService.petInfo();
    }




}
