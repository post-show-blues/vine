package com.post_show_blues.vine.exception;

public class AlreadyExistedEmailException extends RuntimeException{
    public AlreadyExistedEmailException(String message) {
        super(message);
    }
}
