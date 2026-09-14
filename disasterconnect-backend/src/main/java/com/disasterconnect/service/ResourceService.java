package com.disasterconnect.service;

import com.disasterconnect.dto.ResourceRequestDTO;
import com.disasterconnect.dto.ResourceResponseDTO;
import com.disasterconnect.entity.Resource;
import com.disasterconnect.entity.User;
import com.disasterconnect.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public ResourceResponseDTO createResource(
            ResourceRequestDTO requestDTO,
            User ngo) {

        Resource resource = new Resource();

        resource.setName(requestDTO.getName());
        resource.setDescription(requestDTO.getDescription());
        resource.setCategory(requestDTO.getCategory());
        resource.setQuantity(requestDTO.getQuantity());
        resource.setUnit(requestDTO.getUnit());
        resource.setLocation(requestDTO.getLocation());

        // Availability is determined by quantity
        resource.setAvailable(
                requestDTO.getQuantity() > 0
        );

        // Link resource to the authenticated NGO
        resource.setNgo(ngo);

        Resource savedResource =
                resourceRepository.save(resource);

        return convertToResponseDTO(savedResource);
    }

    public ResourceResponseDTO updateResource(
            Long id,
            ResourceRequestDTO requestDTO,
            User authenticatedNgo) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resource not found with id: " + id
                        )
                );

        // Only the NGO that owns the resource can update it
        if (!resource.getNgo().getId()
                .equals(authenticatedNgo.getId())) {

            throw new RuntimeException(
                    "You are not allowed to modify this resource"
            );
        }

        resource.setName(requestDTO.getName());
        resource.setDescription(requestDTO.getDescription());
        resource.setCategory(requestDTO.getCategory());
        resource.setQuantity(requestDTO.getQuantity());
        resource.setUnit(requestDTO.getUnit());
        resource.setLocation(requestDTO.getLocation());

        // Availability is controlled by the server
        resource.setAvailable(
                requestDTO.getQuantity() > 0
        );

        Resource updatedResource =
                resourceRepository.save(resource);

        return convertToResponseDTO(updatedResource);
    }

    // Get all resources owned by the authenticated NGO
    public List<ResourceResponseDTO> getMyResources(
            User authenticatedNgo) {

        return resourceRepository.findAll()
                .stream()
                .filter(resource ->
                        resource.getNgo().getId()
                                .equals(authenticatedNgo.getId())
                )
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Get one resource owned by the authenticated NGO
    public ResourceResponseDTO getResourceById(
            Long id,
            User authenticatedNgo) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resource not found with id: " + id
                        )
                );

        // Only the owning NGO can view this resource
        if (!resource.getNgo().getId()
                .equals(authenticatedNgo.getId())) {

            throw new RuntimeException(
                    "You are not allowed to view this resource"
            );
        }

        return convertToResponseDTO(resource);
    }

    // Only the owning NGO can delete the resource
    public void deleteResource(
            Long id,
            User authenticatedNgo) {

        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resource not found with id: " + id
                        )
                );

        // Ownership check
        if (!resource.getNgo().getId()
                .equals(authenticatedNgo.getId())) {

            throw new RuntimeException(
                    "You are not allowed to delete this resource"
            );
        }

        resourceRepository.delete(resource);
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