package com.login21.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class) // Usuario no encontrado
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", ex.getMessage()
                ));
    }

    @ExceptionHandler(InvalidPasswordException.class) // Contrasena incorrecta
    public ResponseEntity<?> handleInvalidPassword(InvalidPasswordException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", ex.getMessage()
                ));
    }
    @ExceptionHandler(UserAlreadyExistsException.class) // Usuario ya existente
    public ResponseEntity<?> userAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Error interno del servidor"
                ));
    }

    @ExceptionHandler(InvalidPasswordFormatException.class) //Password invalido
    public ResponseEntity<?> handleInvalidPasswordFormat(InvalidPasswordFormatException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", ex.getMessage()
                ));
    }

}
