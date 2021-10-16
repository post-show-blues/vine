package com.post_show_blues.vine.handler.exception;

import java.util.Map;

public class CustomValidationApiException extends RuntimeException{

    private static final long serialVersionUID=1L;

    public Map<String,String> errorMap;

    public CustomValidationApiException(String message, Map<String,String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String,String> getErrorMap(){
        return this.errorMap;
    }
}
