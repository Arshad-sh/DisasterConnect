package com.disasterconnect.dto;

public class LoginResponseDTO {

    private String message;
    private String email;

    public LoginResponseDTO(String message, String email) {
        this.message = message;
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }
}