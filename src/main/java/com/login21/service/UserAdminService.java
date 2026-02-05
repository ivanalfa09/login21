package com.login21.service;

import com.login21.entity.AccessCredential;
import com.login21.entity.Status;
import com.login21.entity.User;
import com.login21.repository.AccessCredentialRepository;
import com.login21.repository.StatusRepository;
import com.login21.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserAdminService {

    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final AccessCredentialRepository credentialRepository;

    public UserAdminService(
            UserRepository userRepository,
            StatusRepository statusRepository,
            AccessCredentialRepository credentialRepository
    ) {
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
        this.credentialRepository = credentialRepository;
    }

    @Transactional
    public void updateUserStatus(Integer userId, String newStatus) {

        
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Status status = statusRepository.findByStatus(newStatus)
                .orElseThrow(() -> new RuntimeException("Status no válido"));

        //  Cambiar status
        user.setStatus(status);

        //  Resetear intentos fallidos
        AccessCredential cred = user.getAccessCredential();
        cred.setFailedAttempts(0);

        credentialRepository.save(cred);
        userRepository.save(user);
    }

}
