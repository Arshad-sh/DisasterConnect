package com.disasterconnect.service;

import com.disasterconnect.dto.NGOProfileRequestDTO;
import com.disasterconnect.dto.NGOProfileResponseDTO;
import com.disasterconnect.entity.NGOProfile;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.VerificationStatus;
import com.disasterconnect.repository.NGOProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class NGOProfileService {

    private final NGOProfileRepository ngoProfileRepository;

    public NGOProfileService(
            NGOProfileRepository ngoProfileRepository) {

        this.ngoProfileRepository = ngoProfileRepository;
    }

    public NGOProfileResponseDTO createProfile(
            NGOProfileRequestDTO requestDTO,
            User user) {

        NGOProfile profile = new NGOProfile();

        profile.setOrganizationName(
                requestDTO.getOrganizationName()
        );

        profile.setDescription(
                requestDTO.getDescription()
        );

        profile.setContactNumber(
                requestDTO.getContactNumber()
        );

        profile.setAddress(
                requestDTO.getAddress()
        );

        // New NGO profiles start as PENDING.
        profile.setVerificationStatus(
                VerificationStatus.PENDING
        );

        // Link profile to authenticated NGO user.
        profile.setUser(user);

        NGOProfile savedProfile =
                ngoProfileRepository.save(profile);

        return convertToResponseDTO(savedProfile);
    }

    public NGOProfileResponseDTO getProfileByUserId(
            Long userId) {

        NGOProfile profile =
                ngoProfileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "NGO profile not found"
                                )
                        );

        return convertToResponseDTO(profile);
    }

    private NGOProfileResponseDTO convertToResponseDTO(
            NGOProfile profile) {

        return new NGOProfileResponseDTO(
                profile.getId(),
                profile.getOrganizationName(),
                profile.getDescription(),
                profile.getContactNumber(),
                profile.getAddress(),
                profile.getVerificationStatus(),
                profile.getUser().getId()
        );
    }
}