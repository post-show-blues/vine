package com.post_show_blues.vine.handler;

import com.post_show_blues.vine.dto.CMRespDto;
import com.post_show_blues.vine.handler.exception.CustomApiException;
import com.post_show_blues.vine.handler.exception.CustomValidationApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(CustomValidationApiException.class)
    public ResponseEntity<CMRespDto<?>> validationApiException(CustomValidationApiException e){

        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),e.errorMap), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<CMRespDto<?>> apiException(CustomApiException e){

        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
    }
}
