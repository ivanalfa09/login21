package com.login21.controller;

import com.login21.model.StatusRequest;
import com.login21.service.UserAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserAdminService userAdminService;

    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    @PutMapping("/status")
    public ResponseEntity<?> updateStatus(@RequestBody StatusRequest request) {

        userAdminService.updateUserStatus(
                request.getUserId(),
                request.getStatus()
        );

        return ResponseEntity.ok().body("Usuario actualizado correctamente");
    }
}
