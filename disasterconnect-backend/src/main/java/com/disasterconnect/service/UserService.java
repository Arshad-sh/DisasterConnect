package com.disasterconnect.service;

import com.disasterconnect.dto.UserRequestDTO;
import com.disasterconnect.dto.UserResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.exception.ResourceNotFoundException;
import com.disasterconnect.mapper.UserMapper;
import com.disasterconnect.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================================================
    // CREATE USER - DTO → ENTITY → PASSWORD ENCODING → DATABASE
    // =========================================================

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        User user = UserMapper.toEntity(userRequestDTO);

        // Encode password using BCrypt before saving
        String encodedPassword =
                passwordEncoder.encode(userRequestDTO.getPassword());

        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }

    // Existing method - kept for DataLoader
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // =========================================================
    // GET USER BY ID
    // =========================================================

    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id
                        )
                );
    }

    // DTO-based GET USER BY ID
    public UserResponseDTO getUserResponseById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id
                        )
                );

        return UserMapper.toResponseDTO(user);
    }

    // =========================================================
    // GET ALL USERS
    // =========================================================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // DTO-based GET ALL USERS
    public List<UserResponseDTO> getAllUserResponses() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // =========================================================
    // UPDATE USER
    // =========================================================

    public User updateUser(Long id, User updatedUser) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id
                        )
                );

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setRole(updatedUser.getRole());

        return userRepository.save(user);
    }

    // DTO-based UPDATE USER
    public UserResponseDTO updateUser(
            Long id,
            UserRequestDTO userRequestDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id
                        )
                );

        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPhone(userRequestDTO.getPhone());
        user.setRole(userRequestDTO.getRole());

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(updatedUser);
    }

    // =========================================================
    // DELETE USER
    // =========================================================

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id
                        )
                );

        userRepository.delete(user);
    }
}