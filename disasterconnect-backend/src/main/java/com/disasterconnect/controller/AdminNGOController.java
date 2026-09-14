package com.disasterconnect.controller;

import com.disasterconnect.dto.NGOProfileResponseDTO;
import com.disasterconnect.entity.NGOProfile;
import com.disasterconnect.enums.VerificationStatus;
import com.disasterconnect.repository.NGOProfileRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ngos")
public class AdminNGOController {

    private final NGOProfileRepository ngoProfileRepository;

    public AdminNGOController(
            NGOProfileRepository ngoProfileRepository) {

        this.ngoProfileRepository = ngoProfileRepository;
    }

    // =========================================================
    // GET ALL NGO PROFILES
    // =========================================================

    @GetMapping
    public List<NGOProfileResponseDTO> getAllNGOs() {

        return ngoProfileRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // =========================================================
    // GET NGO PROFILE BY ID
    // =========================================================

    @PatchMapping("/{id}/verification")
    public NGOProfileResponseDTO verifyNGO(
            @PathVariable Long id) {

        NGOProfile profile = ngoProfileRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("NGO profile not found"));

        profile.setVerificationStatus(VerificationStatus.VERIFIED);

        return convertToResponseDTO(
                ngoProfileRepository.save(profile)
        );
    }

    @GetMapping("/{id}")
    public NGOProfileResponseDTO getNGOById(
            @PathVariable Long id) {

        NGOProfile profile =
                ngoProfileRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "NGO profile not found"
                                )
                        );

        return convertToResponseDTO(profile);
    }

    // =========================================================
    // CONVERT ENTITY TO DTO
    // =========================================================

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