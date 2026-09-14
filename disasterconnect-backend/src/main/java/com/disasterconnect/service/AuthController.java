package com.disasterconnect.service;

import com.disasterconnect.dto.LoginRequestDTO;
import com.disasterconnect.dto.UserRequestDTO;
import com.disasterconnect.dto.UserResponseDTO;
import com.disasterconnect.enums.Role;
import com.disasterconnect.security.JwtService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            UserService userService,
            JwtService jwtService) {

        this.authService = authService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(
            @Valid @RequestBody UserRequestDTO userRequestDTO) {

        // Public registration may create responder/citizen accounts,
        // but administrator accounts are provisioned separately.
        if (userRequestDTO.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Administrator accounts cannot be created through public registration");
        }

        return userService.createUser(userRequestDTO);
    }

    @PostMapping("/login")
    public String login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        var authentication = authService.login(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        );

        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .orElseThrow()
                .getAuthority()
                .replace("ROLE_", "");

        return jwtService.generateToken(
                authentication.getName(),
                role
        );
    }
}