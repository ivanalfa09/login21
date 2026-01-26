package com.login21.service;

import com.login21.entity.AccessCredential;
import com.login21.entity.User;
import com.login21.exception.UserAlreadyExistsException;
import com.login21.repository.AccessCredentialRepository;
import com.login21.repository.UserRepository;
import com.login21.security.PasswordValidator;
import com.login21.util.IdGenerator;
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
    //private final LogService logService;

    public UserRegistrationService(
            UserRepository userRepository,
            AccessCredentialRepository credentialRepository,
            PasswordEncoder passwordEncoder
           // LogService logService
    ) {
        this.userRepository = userRepository;
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
        //this.logService = logService;
    }


    public User register(String username, String rawPassword) {

        if (credentialRepository.findByUser(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        // Validar contraseña antes de guardar
        PasswordValidator.validate(rawPassword);

        AccessCredential cred = new AccessCredential();
        cred.setUser(username);
        cred.setPassword(passwordEncoder.encode(rawPassword));
        cred.setIdUser("DPR-"+generateUniquePublicId());

        cred = credentialRepository.save(cred);

        User user = new User();
        user.setUserLevel(1);
        user.setUuid(UUID.randomUUID().toString());
        user.setAccessCredential(cred);

        return userRepository.save(user);
    }

    private String generateUniquePublicId() {
        String id;
        do {
            id = IdGenerator.generate(8);
        } while (credentialRepository.existsByIdUser(id));
        return id;
    }
}
