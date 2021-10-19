package com.post_show_blues.vine.handler.exception;

public class AlreadyExistedEmailException extends RuntimeException {
    public AlreadyExistedEmailException(String message) {
        super(message);
    }
}
