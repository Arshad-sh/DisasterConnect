package com.disasterconnect.mapper;

import com.disasterconnect.dto.UserRequestDTO;
import com.disasterconnect.dto.UserResponseDTO;
import com.disasterconnect.entity.User;

public class UserMapper {

    private UserMapper() {
        // Utility class
    }

    // UserRequestDTO → User Entity
    public static User toEntity(UserRequestDTO dto) {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());

        return user;
    }

    // User Entity → UserResponseDTO
    public static UserResponseDTO toResponseDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());

        return dto;
    }
}