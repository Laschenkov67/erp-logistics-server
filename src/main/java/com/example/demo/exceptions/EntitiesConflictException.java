package com.example.demo.exceptions;

public class EntitiesConflictException extends RuntimeException {

    public EntitiesConflictException() {
        super();
    }

    public EntitiesConflictException(String msg) {
        super(msg);
    }
}
