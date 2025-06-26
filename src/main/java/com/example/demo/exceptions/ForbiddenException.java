package com.example.demo.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super();
    }
    public ForbiddenException(String msg) {
        super(msg);
    }
}
