package com.login21.service;

import com.login21.entity.AccessCredential;
import com.login21.exception.InvalidPasswordException;
import com.login21.exception.UserNotFoundException;
import com.login21.repository.AccessCredentialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AccessCredentialRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    public AuthService(
            AccessCredentialRepository repository,
            PasswordEncoder passwordEncoder, LogService logService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }
    public AccessCredential login(String user, String password) {

        AccessCredential cred = repository.findByUser(user)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, cred.getPassword())) {
            throw new InvalidPasswordException();
        }
        return cred;
        /*
        logService.log(
                cred.getId(),     // o idUser si lo tienes mapeado
                "LOGIN",
                cred.getUser(),
                cred.getId(),
                "Inicio de sesión exitoso",
                "SYSTEM"
        );
         */
    }
}

