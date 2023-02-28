package com.example.demo.controllers;

import com.example.demo.dtos.ResponseDTO;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AbstractController<T>  {

    public enum LogStatus {
        INFO,
        ERROR
    }
    private static final String BASE_API = "/api" ;
    protected static final String FILE_API = BASE_API + "/file";
    ResponseEntity<ResponseDTO<T>> getResponseDTO(T dto, String message, Logger logger, HttpStatus status , LogStatus logStatus) {
        if(LogStatus.ERROR.equals(logStatus)) logger.error(message);
        if(LogStatus.INFO.equals(logStatus)) logger.info(message);
        ResponseDTO<T> responseDTO = new ResponseDTO<>(message, dto);
        return ResponseEntity.status(status).body(responseDTO);
    }

}
