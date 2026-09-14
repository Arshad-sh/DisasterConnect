package com.disasterconnect.controller;

import com.disasterconnect.dto.ResourceResponseDTO;
import com.disasterconnect.entity.Resource;
import com.disasterconnect.repository.ResourceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/resources")
public class AdminResourceController {

    private final ResourceRepository resourceRepository;

    public AdminResourceController(
            ResourceRepository resourceRepository) {

        this.resourceRepository = resourceRepository;
    }

    @GetMapping
    public List<ResourceResponseDTO> getAllResources() {

        return resourceRepository
                .findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResourceResponseDTO getResourceById(
            @PathVariable Long id) {

        Resource resource =
                resourceRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resource not found with id: " + id
                                )
                        );

        return convertToResponseDTO(resource);
    }

    private ResourceResponseDTO convertToResponseDTO(
            Resource resource) {

        return new ResourceResponseDTO(
                resource.getId(),
                resource.getName(),
                resource.getDescription(),
                resource.getCategory(),
                resource.getQuantity(),
                resource.getUnit(),
                resource.isAvailable(),
                resource.getLocation(),
                resource.getNgo().getId()
        );
    }
}