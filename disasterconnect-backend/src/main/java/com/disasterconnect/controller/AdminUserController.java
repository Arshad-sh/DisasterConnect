package com.disasterconnect.controller;

import com.disasterconnect.entity.User;
import com.disasterconnect.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;

    public AdminUserController(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    // =========================================================
    // GET ALL USERS
    // =========================================================

    @GetMapping
    public List<UserResponse> getAllUsers() {

        return userRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // =========================================================
    // GET USER BY ID
    // =========================================================

    @GetMapping("/{id}")
    public UserResponse getUserById(
            @PathVariable Long id) {

        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found with id: " + id
                                )
                        );

        return convertToResponse(user);
    }

    // =========================================================
    // CONVERT ENTITY TO RESPONSE
    // =========================================================

    private UserResponse convertToResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole().name()
        );
    }

    // =========================================================
    // ADMIN USER RESPONSE
    // =========================================================

    public record UserResponse(
            Long id,
            String name,
            String email,
            String phone,
            String role
    ) {
    }
}