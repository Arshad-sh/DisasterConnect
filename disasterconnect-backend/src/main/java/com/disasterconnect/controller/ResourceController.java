package com.disasterconnect.controller;

import com.disasterconnect.dto.ResourceRequestDTO;
import com.disasterconnect.dto.ResourceResponseDTO;
import com.disasterconnect.entity.User;
import com.disasterconnect.repository.UserRepository;
import com.disasterconnect.service.ResourceService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ngo/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final UserRepository userRepository;

    public ResourceController(
            ResourceService resourceService,
            UserRepository userRepository) {

        this.resourceService = resourceService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResourceResponseDTO createResource(
            @Valid @RequestBody ResourceRequestDTO requestDTO,
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return resourceService.createResource(
                requestDTO,
                ngo
        );
    }

    @PutMapping("/{id}")
    public ResourceResponseDTO updateResource(
            @PathVariable Long id,
            @Valid @RequestBody ResourceRequestDTO requestDTO,
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return resourceService.updateResource(
                id,
                requestDTO,
                ngo
        );
    }

    @GetMapping
    public List<ResourceResponseDTO> getMyResources(
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return resourceService.getMyResources(ngo);
    }

    @GetMapping("/{id}")
    public ResourceResponseDTO getResourceById(
            @PathVariable Long id,
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        return resourceService.getResourceById(
                id,
                ngo
        );
    }

    @DeleteMapping("/{id}")
    public void deleteResource(
            @PathVariable Long id,
            Authentication authentication) {

        User ngo = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );

        resourceService.deleteResource(
                id,
                ngo
        );
    }
}