package com.login21.controller;

import com.login21.entity.AccessCredential;
import com.login21.entity.User;
import com.login21.model.LoginRequest;
import com.login21.model.RegisterRequest;
import com.login21.service.AuthService;
import com.login21.service.UserRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final AuthService authService;

    public AuthController(UserRegistrationService userRegistrationService, AuthService authService) {
        this.userRegistrationService = userRegistrationService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        User user = userRegistrationService.register(
                request.getUser(),
                request.getPassword()
        );


        return ResponseEntity.ok(
                Map.of(
                        "message:", "Usuario creado",
                        "IdUser:", user.getAccessCredential().getIdUser()
                )
        );    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

       AccessCredential cred = authService.login(
                request.getUser(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                Map.of("message:", "Login exitoso")
        );
    }

}

