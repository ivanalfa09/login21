package com.login21.service;

import com.login21.entity.AccessCredential;
import com.login21.entity.Status;
import com.login21.entity.User;
import com.login21.exception.InvalidPasswordException;
import com.login21.exception.UserBlockedException;
import com.login21.exception.UserNotFoundException;
import com.login21.repository.AccessCredentialRepository;
import com.login21.repository.StatusRepository;
import com.login21.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final int MAX_ATTEMPTS = 3;

    private final AccessCredentialRepository repository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AccessCredentialRepository repository,
            UserRepository userRepository,
            StatusRepository statusRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AccessCredential login(String user, String password) {

        AccessCredential cred = repository.findByUser(user)
                .orElseThrow(UserNotFoundException::new);

        User appUser = userRepository.findById(
                Long.valueOf(cred.getId())
        ).orElseThrow();

        // 🚫 Usuario bloqueado
        if ("bloqueado".equalsIgnoreCase(appUser.getStatus().getStatus())) {
            throw new UserBlockedException();
        }

        // ❌ Password incorrecta
        if (!passwordEncoder.matches(password, cred.getPassword())) {

            int attempts = cred.getFailedAttempts() + 1;
            cred.setFailedAttempts(attempts);

            // 🔒 Bloquear usuario
            if (attempts >= MAX_ATTEMPTS) {
                Status blocked = statusRepository
                        .findByStatus("bloqueado")
                        .orElseThrow();

                appUser.setStatus(blocked);
            }

            repository.save(cred);
            userRepository.save(appUser);

            throw new InvalidPasswordException();
        }

        // ✅ Login exitoso → resetear intentos
        cred.setFailedAttempts(0);
        repository.save(cred);

        return cred;
    }
}
