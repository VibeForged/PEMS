package com.example.pems.controller;

import com.example.pems.dto.LoginRequest;
import com.example.pems.entity.User;
import com.example.pems.service.JwtService;
import com.example.pems.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        Optional<User> userOptional =
                userService.getUserByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "message",
                            "Invalid email or password"
                    ));
        }

        User user = userOptional.get();

        boolean passwordMatches =
                userService.verifyPassword(
                        loginRequest.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "message",
                            "Invalid email or password"
                    ));
        }

        if (!user.isActive()) {
            return ResponseEntity.status(403).body(Map.of(
                    "message", "This account has been deactivated"
            ));
        }

        String token =
                jwtService.generateToken(user.getEmail());

        Map<String, Object> response =
                new HashMap<>();

        response.put("message", "Login successful");
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
