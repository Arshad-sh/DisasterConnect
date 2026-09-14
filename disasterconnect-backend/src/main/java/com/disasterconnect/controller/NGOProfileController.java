package com.disasterconnect.controller;

import com.disasterconnect.dto.NGOProfileRequestDTO;
import com.disasterconnect.dto.NGOProfileResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.NGOProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ngo/profile")
public class NGOProfileController {

    private final NGOProfileService ngoProfileService;
    private final UserRepository userRepository;

    public NGOProfileController(
            NGOProfileService ngoProfileService,
            UserRepository userRepository) {

        this.ngoProfileService = ngoProfileService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public NGOProfileResponseDTO createProfile(
            @Valid @RequestBody NGOProfileRequestDTO requestDTO,
            Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return ngoProfileService.createProfile(
                requestDTO,
                user
        );
    }

    @GetMapping
    public NGOProfileResponseDTO getMyProfile(
            Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return ngoProfileService.getProfileByUserId(
                user.getId()
        );
    }
}