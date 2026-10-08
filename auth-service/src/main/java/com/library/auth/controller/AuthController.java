package com.library.auth.controller;

import com.library.auth.model.LoginRequest;
import com.library.auth.model.User;
import com.library.auth.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = getUser(request.getUsername());

        if (user == null ||
                !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid username or password"));
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "username", user.getUsername(),
                "role", user.getRole(),
                "token", token
        ));
    }

    private User getUser(String username) {
        String adminHash = passwordEncoder.encode("admin123");
        String userHash = passwordEncoder.encode("user123");

        if ("admin".equals(username)) {
            return new User("admin", adminHash, "ADMIN");
        }

        if ("user".equals(username)) {
            return new User("user", userHash, "USER");
        }

        if ("librarian".equals(username)) {
            return new User("librarian", adminHash, "LIBRARIAN");
        }

        return null;
    }
}
