package com.disasterconnect.service;

import com.disasterconnect.dto.VolunteerProfileRequestDTO;
import com.disasterconnect.dto.VolunteerProfileResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.entity.VolunteerProfile;
import com.disasterconnect.enums.VerificationStatus;
import com.disasterconnect.repository.VolunteerProfileRepository;
import com.disasterconnect.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class VolunteerProfileService {

    private final VolunteerProfileRepository volunteerProfileRepository;

    public VolunteerProfileService(
            VolunteerProfileRepository volunteerProfileRepository) {

        this.volunteerProfileRepository =
                volunteerProfileRepository;
    }

    public VolunteerProfileResponseDTO createProfile(
            VolunteerProfileRequestDTO requestDTO,
            User volunteer) {

        VolunteerProfile profile =
                new VolunteerProfile();

        profile.setSkills(requestDTO.getSkills());
        profile.setLocation(requestDTO.getLocation());

        // Availability is controlled by the server
        profile.setAvailability(true);

        // New profiles require verification
        profile.setVerificationStatus(
                VerificationStatus.PENDING
        );

        // Link profile to authenticated volunteer
        profile.setUser(volunteer);

        VolunteerProfile savedProfile =
                volunteerProfileRepository.save(profile);

        return convertToResponseDTO(savedProfile);
    }

    public VolunteerProfileResponseDTO updateAvailability(Long userId, boolean availability) {
        VolunteerProfile profile = volunteerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer profile not found"));

        profile.setAvailability(availability);
        VolunteerProfile savedProfile = volunteerProfileRepository.save(profile);
        return convertToResponseDTO(savedProfile);
    }

    public VolunteerProfileResponseDTO getProfileByUserId(
            Long userId) {

        VolunteerProfile profile =
                volunteerProfileRepository
                        .findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
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