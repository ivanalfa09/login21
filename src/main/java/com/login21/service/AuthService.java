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

    public AuthService(
            AccessCredentialRepository repository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(String user, String password) {

        AccessCredential cred = repository.findByUser(user)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, cred.getPassword())) {
            throw new InvalidPasswordException();
        }
    }
}

