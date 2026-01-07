package com.login21.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Contraseña incorrecta");
    }
}
