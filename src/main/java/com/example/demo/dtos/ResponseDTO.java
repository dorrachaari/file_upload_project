package com.example.demo.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO<T> {

    private String message;
    private T data;


}
