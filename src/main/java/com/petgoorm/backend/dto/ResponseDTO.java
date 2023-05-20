package com.petgoorm.backend.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
//공통 응답 DTO
public class ResponseDTO<T> {
    private final int statusCode;
    private final String message;
    private final T data;
}
