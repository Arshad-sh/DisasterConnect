package com.disasterconnect.controller;

import com.disasterconnect.dto.VolunteerProfileRequestDTO;
import com.disasterconnect.dto.VolunteerProfileResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.VolunteerProfileService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/volunteer/profile")
public class VolunteerProfileController {

    private final VolunteerProfileService volunteerProfileService;
    private final UserRepository userRepository;

    public VolunteerProfileController(
            VolunteerProfileService volunteerProfileService,
            UserRepository userRepository) {

        this.volunteerProfileService =
                volunteerProfileService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public VolunteerProfileResponseDTO createProfile(
            @Valid @RequestBody VolunteerProfileRequestDTO requestDTO,
            Authentication authentication) {

        User volunteer = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return volunteerProfileService.createProfile(
                requestDTO,
                volunteer
        );
    }

    @PatchMapping("/availability")
    public VolunteerProfileResponseDTO updateAvailability(
            @RequestParam boolean availability,
            Authentication authentication) {

        User volunteer = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        return volunteerProfileService.updateAvailability(volunteer.getId(), availability);
    }

    @GetMapping
    public VolunteerProfileResponseDTO getMyProfile(
            Authentication authentication) {

        User volunteer = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return volunteerProfileService.getProfileByUserId(
                volunteer.getId()
        );
    }
}