package com.disasterconnect.service;

import com.disasterconnect.dto.ReservationRequestDTO;
import com.disasterconnect.entity.Request;
import com.disasterconnect.entity.Resource;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.enums.ResourceCategory;
import com.disasterconnect.enums.Role;
import com.disasterconnect.enums.Urgency;
import com.disasterconnect.repository.RequestRepository;
import com.disasterconnect.repository.ReservationRepository;
import com.disasterconnect.repository.ResourceRepository;
import com.disasterconnect.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class ReservationServiceRollbackTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @MockitoBean
    private ReservationRepository reservationRepository;

    private Long userId;
    private Long requestId;
    private Long resourceId;

    @AfterEach
    void cleanup() {

        if (resourceId != null) {
            resourceRepository.deleteById(resourceId);
        }

        if (requestId != null) {
            requestRepository.deleteById(requestId);
        }

        if (userId != null) {
            userRepository.deleteById(userId);
        }
    }

    @Test
    void createReservation_shouldRollbackResourceQuantityWhenReservationSaveFails() {

        // Create test NGO
        User ngo = new User();
        ngo.setName("Rollback Test NGO");
        ngo.setEmail("rollback-" + System.nanoTime() + "@test.com");
        ngo.setPassword("test123");
        ngo.setRole(Role.NGO);

        ngo = userRepository.save(ngo);
        userId = ngo.getId();

        // Create test request
        Request request = new Request();
        request.setTitle("Rollback Test Request");
        request.setDescription("Testing transaction rollback");
        request.setLocation("Test Location");
        request.setUrgency(Urgency.LOW);
        request.setStatus(RequestStatus.CREATED);
        request.setUser(ngo);

        request = requestRepository.save(request);
        requestId = request.getId();

        // Create test resource
        Resource resource = new Resource();
        resource.setName("Rollback Test Resource");
        resource.setDescription("Testing transaction rollback");
        resource.setCategory(ResourceCategory.values()[0]);
        resource.setQuantity(10);
        resource.setUnit("units");
        resource.setAvailable(true);
        resource.setLocation("Test Location");
        resource.setNgo(ngo);

        resource = resourceRepository.save(resource);
        resourceId = resource.getId();

        int originalQuantity = resource.getQuantity();

        // Create reservation request
        ReservationRequestDTO requestDTO =
                new ReservationRequestDTO();

        requestDTO.setRequestId(requestId);
        requestDTO.setResourceId(resourceId);
        requestDTO.setQuantity(4);

        // Force reservation save to fail
        doThrow(new RuntimeException("Simulated reservation failure"))
                .when(reservationRepository)
                .save(any());

        // Make NGO effectively final for lambda
        User savedNgo = ngo;

        // Service should throw the exception
        assertThrows(
                RuntimeException.class,
                () -> reservationService.createReservation(
                        requestDTO,
                        savedNgo
                )
        );

        // Reload resource from database
        Resource resourceAfterRollback =
                resourceRepository.findById(resourceId)
                        .orElseThrow();

        // Resource quantity must remain unchanged
        assertEquals(
                originalQuantity,
                resourceAfterRollback.getQuantity()
        );
    }
}