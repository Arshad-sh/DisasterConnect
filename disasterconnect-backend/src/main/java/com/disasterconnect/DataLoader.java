package com.disasterconnect;

import com.disasterconnect.dto.UserRequestDTO;
import com.disasterconnect.dto.UserResponseDTO;
import com.disasterconnect.entity.Request;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.enums.Role;
import com.disasterconnect.enums.Urgency;
import com.disasterconnect.repository.RequestRepository;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(
            UserService userService,
            RequestRepository requestRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // =====================================================
        // 0. ENSURE ADMIN USER EXISTS
        // =====================================================

        String adminEmail =
                "admin@disasterconnect.test";

        String adminPassword =
                "Admin@12345";

        Optional<User> existingAdmin =
                userRepository.findByEmail(adminEmail);

        if (existingAdmin.isEmpty()) {

            User admin = new User();

            admin.setName(
                    "Admin User"
            );

            admin.setEmail(
                    adminEmail
            );

            admin.setPhone(
                    "9876543213"
            );

            admin.setPassword(
                    passwordEncoder.encode(
                            adminPassword
                    )
            );

            admin.setRole(
                    Role.ADMIN
            );

            User savedAdmin =
                    userRepository.save(admin);

            System.out.println(
                    "===== ADMIN USER CREATED ====="
            );

            System.out.println(
                    "Admin ID: " +
                    savedAdmin.getId()
            );

            System.out.println(
                    "Admin Email: " +
                    savedAdmin.getEmail()
            );

            System.out.println(
                    "Admin Role: " +
                    savedAdmin.getRole()
            );

        } else {

            User admin =
                    existingAdmin.get();

            admin.setName(
                    "Admin User"
            );

            admin.setPhone(
                    "9876543213"
            );

            admin.setRole(
                    Role.ADMIN
            );

            admin.setPassword(
                    passwordEncoder.encode(
                            adminPassword
                    )
            );

            userRepository.save(admin);

            System.out.println(
                    "===== ADMIN USER UPDATED ====="
            );

            System.out.println(
                    "Admin ID: " +
                    admin.getId()
            );

            System.out.println(
                    "Admin Email: " +
                    admin.getEmail()
            );

            System.out.println(
                    "Admin Role: " +
                    admin.getRole()
            );
        }

        // =====================================================
        // 1. CREATE USER
        // =====================================================

        System.out.println(
                "===== USER SERVICE TEST ====="
        );

        User user = new User();

        user.setName(
                "Arshad"
        );

        user.setEmail(
                "arshad@example.com"
        );

        user.setPhone(
                "9876543210"
        );

        user.setRole(
                Role.CITIZEN
        );

        User savedUser =
                userService.createUser(user);

        System.out.println(
                "User created with ID: " +
                savedUser.getId()
        );

        // =====================================================
        // 2. FIND USER BY ID
        // =====================================================

        System.out.println(
                "Finding user by ID:"
        );

        User foundUser =
                userService.getUserById(
                        savedUser.getId()
                );

        System.out.println(
                foundUser.getId() + " | " +
                foundUser.getName() + " | " +
                foundUser.getEmail() + " | " +
                foundUser.getPhone() + " | " +
                foundUser.getRole()
        );

        // =====================================================
        // 3. GET ALL USERS
        // =====================================================

        System.out.println(
                "Getting all users:"
        );

        List<User> users =
                userService.getAllUsers();

        users.forEach(u ->
                System.out.println(
                        u.getId() + " | " +
                        u.getName() + " | " +
                        u.getEmail() + " | " +
                        u.getRole()
                )
        );

        // =====================================================
        // 4. UPDATE USER
        // =====================================================

        User updatedUser =
                new User();

        updatedUser.setName(
                "Arshad Updated"
        );

        updatedUser.setEmail(
                "arshad.updated@example.com"
        );

        updatedUser.setPhone(
                "9999999999"
        );

        updatedUser.setRole(
                Role.CITIZEN
        );

        userService.updateUser(
                savedUser.getId(),
                updatedUser
        );

        System.out.println(
                "User updated successfully!"
        );

        // =====================================================
        // 5. CREATE REQUEST
        // =====================================================

        System.out.println(
                "===== REQUEST PERSISTENCE TEST ====="
        );

        Request request =
                new Request();

        request.setTitle(
                "Need Medical Supplies"
        );

        request.setDescription(
                "Emergency medical supplies required."
        );

        request.setLocation(
                "Pune"
        );

        request.setUrgency(
                Urgency.HIGH
        );

        request.setStatus(
                RequestStatus.CREATED
        );

        request.setUser(
                savedUser
        );

        Request savedRequest =
                requestRepository.save(
                        request
                );

        System.out.println(
                "Request created with ID: " +
                savedRequest.getId()
        );

        System.out.println(
                "Request linked to User ID: " +
                savedRequest.getUser().getId()
        );

        System.out.println(
                "Request title: " +
                savedRequest.getTitle()
        );

        System.out.println(
                "Request status: " +
                savedRequest.getStatus()
        );

        System.out.println(
                "Request urgency: " +
                savedRequest.getUrgency()
        );

        System.out.println(
                "===== REQUEST PERSISTENCE TEST COMPLETE ====="
        );

        // =====================================================
        // 6. RETRIEVE REQUEST FROM DATABASE
        // =====================================================

        System.out.println(
                "===== REQUEST RETRIEVAL TEST ====="
        );

        Optional<Request> foundRequest =
                requestRepository.findById(
                        savedRequest.getId()
                );

        foundRequest.ifPresent(r -> {

            System.out.println(
                    "Retrieved Request ID: " +
                    r.getId()
            );

            System.out.println(
                    "Retrieved Request Title: " +
                    r.getTitle()
            );

            System.out.println(
                    "Retrieved User ID: " +
                    r.getUser().getId()
            );

            System.out.println(
                    "Retrieved Request Status: " +
                    r.getStatus()
            );

            System.out.println(
                    "Retrieved Request Urgency: " +
                    r.getUrgency()
            );
        });

        System.out.println(
                "===== REQUEST RETRIEVAL TEST COMPLETE ====="
        );

        // =====================================================
        // 7. VERIFY USER -> REQUESTS RELATIONSHIP
        // =====================================================

        System.out.println(
                "===== USER REQUESTS RELATIONSHIP TEST ====="
        );

        User userWithRequests =
                userRepository.findUserWithRequestsById(
                        savedUser.getId()
                );

        if (userWithRequests != null) {

            System.out.println(
                    "User ID: " +
                    userWithRequests.getId()
            );

            System.out.println(
                    "User Name: " +
                    userWithRequests.getName()
            );

            System.out.println(
                    "Number of Requests: " +
                    userWithRequests
                            .getRequests()
                            .size()
            );

            userWithRequests
                    .getRequests()
                    .forEach(r ->
                            System.out.println(
                                    "Request ID: " +
                                    r.getId() +
                                    " | Title: " +
                                    r.getTitle() +
                                    " | Status: " +
                                    r.getStatus()
                            )
                    );
        }

        System.out.println(
                "===== USER REQUESTS RELATIONSHIP TEST COMPLETE ====="
        );

        // =====================================================
        // 8. VERIFY REQUEST -> USER RELATIONSHIP
        // =====================================================

        System.out.println(
                "===== REQUEST USER RELATIONSHIP TEST ====="
        );

        Optional<Request> requestWithUser =
                requestRepository.findById(
                        savedRequest.getId()
                );

        requestWithUser.ifPresent(r -> {

            System.out.println(
                    "Request ID: " +
                    r.getId()
            );

            System.out.println(
                    "Request Title: " +
                    r.getTitle()
            );

            System.out.println(
                    "Linked User ID: " +
                    r.getUser().getId()
            );

            System.out.println(
                    "Linked User Name: " +
                    r.getUser().getName()
            );

            System.out.println(
                    "Linked User Email: " +
                    r.getUser().getEmail()
            );
        });

        System.out.println(
                "===== REQUEST USER RELATIONSHIP TEST COMPLETE ====="
        );

        // =====================================================
        // 9. DTO -> MAPPER -> SERVICE -> DATABASE TEST
        // =====================================================

        System.out.println(
                "===== DTO SERVICE TEST ====="
        );

        UserRequestDTO userRequestDTO =
                new UserRequestDTO();

        userRequestDTO.setName(
                "DTO Test User"
        );

        userRequestDTO.setEmail(
                "dto.test@example.com"
        );

        userRequestDTO.setPhone(
                "8888888888"
        );

        userRequestDTO.setRole(
                Role.CITIZEN
        );

        UserResponseDTO userResponseDTO =
                userService.createUser(
                        userRequestDTO
                );

        System.out.println(
                "DTO User ID: " +
                userResponseDTO.getId()
        );

        System.out.println(
                "DTO User Name: " +
                userResponseDTO.getName()
        );

        System.out.println(
                "DTO User Email: " +
                userResponseDTO.getEmail()
        );

        System.out.println(
                "DTO User Phone: " +
                userResponseDTO.getPhone()
        );

        System.out.println(
                "DTO User Role: " +
                userResponseDTO.getRole()
        );

        System.out.println(
                "===== DTO SERVICE TEST COMPLETE ====="
        );

        System.out.println(
                "===== USER SERVICE TEST COMPLETE ====="
        );
    }
}