package com.login21.service;

import com.login21.entity.AccessCredential;
import com.login21.entity.User;
import com.login21.exception.UserAlreadyExistsException;
import com.login21.repository.AccessCredentialRepository;
import com.login21.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final AccessCredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(
            UserRepository userRepository,
            AccessCredentialRepository credentialRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String username, String rawPassword) {

        if (credentialRepository.findByUser(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        /*Crear credenciales*/
        AccessCredential cred = new AccessCredential();
        cred.setUser(username);
        cred.setPassword(passwordEncoder.encode(rawPassword));

        cred = credentialRepository.save(cred);

        /*Crear usuario*/
        User user = new User();
        user.setUserLevel(1); // default
        user.setUuid(UUID.randomUUID().toString());
        user.setAccessCredential(cred);

        return userRepository.save(user);
    }
}
