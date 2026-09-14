package com.disasterconnect.controller;

import com.disasterconnect.dto.VolunteerProfileResponseDTO;
import com.disasterconnect.entity.VolunteerProfile;
import com.disasterconnect.enums.VerificationStatus;
import com.disasterconnect.repository.VolunteerProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/volunteers")
public class AdminVolunteerController {

    private final VolunteerProfileRepository volunteerProfileRepository;

    public AdminVolunteerController(
            VolunteerProfileRepository volunteerProfileRepository) {

        this.volunteerProfileRepository =
                volunteerProfileRepository;
    }

    @GetMapping
    public List<VolunteerProfileResponseDTO> getAllVolunteers() {

        return volunteerProfileRepository
                .findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @PatchMapping("/{id}/verification")
    public VolunteerProfileResponseDTO verifyVolunteer(
            @PathVariable Long id) {

        VolunteerProfile profile = volunteerProfileRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Volunteer profile not found"));

        profile.setVerificationStatus(VerificationStatus.VERIFIED);

        return convertToResponseDTO(
                volunteerProfileRepository.save(profile)
        );
    }

    @GetMapping("/{id}")
    public VolunteerProfileResponseDTO getVolunteerById(
            @PathVariable Long id) {

        VolunteerProfile profile =
                volunteerProfileRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Volunteer profile not found"
                                )
                        );

        return convertToResponseDTO(profile);
    }

    private VolunteerProfileResponseDTO convertToResponseDTO(
            VolunteerProfile profile) {

        return new VolunteerProfileResponseDTO(
                profile.getId(),
                profile.getSkills(),
                profile.getLocation(),
                profile.isAvailability(),
                profile.getVerificationStatus(),
                profile.getUser().getId()
        );
    }
}