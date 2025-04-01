package ru.glebov.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.glebov.jwt.service.AuthService;

@RestController
public class MainController {

    private final AuthService authService;

    public MainController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/main/user")
    public ResponseEntity<String> userPoint() {
        return ResponseEntity.ok("Point of User");
    }

    @GetMapping("/main/moderator")
    public ResponseEntity<String> moderatorPoint() {
        return ResponseEntity.ok("Point of Moderator");
    }

    @GetMapping("/main/superadmin")
    public ResponseEntity<String> superAdminPoint() {
        return ResponseEntity.ok("Point of Super Admin");
    }

    @PostMapping("/admin/unblock/{email}")
    public ResponseEntity<String> unblockUser(@PathVariable String email) {
        boolean unblocked = authService.unblockUser(email);
        if (unblocked) {
            return ResponseEntity.ok("User unblocked");
        } else {
            return ResponseEntity.ok("Cannot unblock user");
        }
    }
}
