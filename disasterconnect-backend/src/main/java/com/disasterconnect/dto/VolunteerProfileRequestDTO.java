package com.disasterconnect.dto;

import jakarta.validation.constraints.NotBlank;

public class VolunteerProfileRequestDTO {

    @NotBlank(message = "Skills are required")
    private String skills;

    @NotBlank(message = "Location is required")
    private String location;

    public VolunteerProfileRequestDTO() {
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}