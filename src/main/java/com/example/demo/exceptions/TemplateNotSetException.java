package com.example.demo.exceptions;

public class TemplateNotSetException extends RuntimeException {
    public TemplateNotSetException(String message) {
        super(message);
    }
}