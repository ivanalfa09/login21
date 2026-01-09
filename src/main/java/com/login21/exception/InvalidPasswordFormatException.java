package com.login21.exception;

public class InvalidPasswordFormatException extends RuntimeException {
    public InvalidPasswordFormatException(String message) {
        super(message);
    }
}
