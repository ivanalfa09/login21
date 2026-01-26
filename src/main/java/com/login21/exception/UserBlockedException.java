package com.login21.exception;

public class UserBlockedException extends RuntimeException {
    public UserBlockedException() {
        super("El usuario está bloqueado por múltiples intentos fallidos");
    }
}
