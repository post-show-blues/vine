package com.post_show_blues.vine.handler.exception;

public class AlreadyExistedNicknameException extends RuntimeException {
    public AlreadyExistedNicknameException(String message) {
        super(message);
    }
}
