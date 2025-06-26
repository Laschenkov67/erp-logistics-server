package com.example.demo.exceptions;

public class NotAManagerException extends RuntimeException {
    public NotAManagerException() {
        super();
    }
    public NotAManagerException(String message) {
        super(message);
    }
}
