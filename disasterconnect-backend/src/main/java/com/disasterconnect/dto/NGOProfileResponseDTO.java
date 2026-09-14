package com.disasterconnect.dto;

import com.disasterconnect.enums.VerificationStatus;

public class NGOProfileResponseDTO {

    private Long id;
    private String organizationName;
    private String description;
    private String contactNumber;
    private String address;
    private VerificationStatus verificationStatus;
    private Long userId;

    public NGOProfileResponseDTO(
            Long id,
            String organizationName,
            String description,
            String contactNumber,
            String address,
            VerificationStatus verificationStatus,
            Long userId) {

        this.id = id;
        this.organizationName = organizationName;
        this.description = description;
        this.contactNumber = contactNumber;
        this.address = address;
        this.verificationStatus = verificationStatus;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getDescription() {
        return description;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public Long getUserId() {
        return userId;
    }
}