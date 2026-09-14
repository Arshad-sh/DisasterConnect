package com.disasterconnect.dto;

import com.disasterconnect.enums.VerificationStatus;

public class VolunteerProfileResponseDTO {

    private Long id;
    private String skills;
    private String location;
    private boolean availability;
    private VerificationStatus verificationStatus;
    private Long userId;

    public VolunteerProfileResponseDTO(
            Long id,
            String skills,
            String location,
            boolean availability,
            VerificationStatus verificationStatus,
            Long userId) {

        this.id = id;
        this.skills = skills;
        this.location = location;
        this.availability = availability;
        this.verificationStatus = verificationStatus;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getSkills() {
        return skills;
    }

    public String getLocation() {
        return location;
    }

    public boolean isAvailability() {
        return availability;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public Long getUserId() {
        return userId;
    }
}