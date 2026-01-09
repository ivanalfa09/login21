package com.login21.security;

import com.login21.exception.InvalidPasswordFormatException;

public class PasswordValidator {

    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

    public static void validate(String password) {

        if (password.length() < 8) {
            throw new InvalidPasswordFormatException(
                    "La contraseña debe tener al menos 8 caracteres"
            );
        }

        if (!password.matches(PASSWORD_REGEX)) {
            throw new InvalidPasswordFormatException(
                    "La contraseña debe contener mayúsculas, minúsculas y números"
            );
        }

        if (CommonPasswords.isCommon(password)) {
            throw new InvalidPasswordFormatException(
                    "La contraseña es demasiado común"
            );
        }
    }
}
