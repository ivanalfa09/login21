package com.login21.security;

import java.util.Set;

public class CommonPasswords {

    public static final Set<String> COMMON_PASSWORDS = Set.of(
            "123456",
            "password",
            "qwerty",
            "admin",
            "admin123",
            "12345678",
            "welcome",
            "letmein",
            "password1"
    );

    public static boolean isCommon(String password) {
        return COMMON_PASSWORDS.contains(password.toLowerCase());
    }
}