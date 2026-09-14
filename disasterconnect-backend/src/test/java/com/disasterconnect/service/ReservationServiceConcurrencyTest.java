package com.disasterconnect.service;

import com.disasterconnect.dto.ReservationRequestDTO;
import com.disasterconnect.dto.ReservationResponseDTO;
import com.disasterconnect.entity.Request;
import com.disasterconnect.entity.Resource;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.enums.ResourceCategory;
import com.disasterconnect.enums.Role;
import com.disasterconnect.enums.Urgency;
import com.disasterconnect.repository.AuditLogRepository;
import com.disasterconnect.repository.RequestRepository;
import com.disasterconnect.repository.ReservationRepository;
import com.disasterconnect.repository.ResourceRepository;
import com.disasterconnect.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private Long userId;
    private Long requestId;
    private Long resourceId;

    private final List<Long> reservationIds =
            new ArrayList<>();

    @AfterEach
    void cleanup() {

        transactionTemplate.executeWithoutResult(status -> {

            for (Long reservationId : reservationIds) {
                reservationRepository.deleteById(
                        reservationId
                );
            }

            // Delete audit logs belonging to the
            // test NGO before deleting the user.
            if (userId != null) {
                auditLogRepository
                        .deleteAllByPerformedBy_Id(userId);
            }

            if (resourceId != null) {
                resourceRepository.deleteById(
                        resourceId
                );
            }

            if (requestId != null) {
                requestRepository.deleteById(
                        requestId
                );
            }

            if (userId != null) {
                userRepository.deleteById(
                        userId
                );
            }
        });
    }

    @Test
    void concurrentReservations_shouldNotOversubscribeResource()
            throws Exception {

        // Create test NGO
        User ngo = new User();

        ngo.setName(
                "Concurrency Test NGO"
        );

        ngo.setEmail(
                "concurrency-" + System.nanoTime()
                        + "@test.com"
        );

        ngo.setPassword("test123");
        ngo.setRole(Role.NGO);

        ngo = userRepository.save(ngo);
        userId = ngo.getId();

        // Create test request
        Request request = new Request();

        request.setTitle(
                "Concurrency Test Request"
        );

        request.setDescription(
                "Testing concurrent reservations"
        );

        request.setLocation(
                "Test Location"
        );

        request.setUrgency(
                Urgency.HIGH
        );

        request.setStatus(
                RequestStatus.CREATED
        );

        request.setUser(ngo);

        request = requestRepository.save(request);
        requestId = request.getId();

        // Create resource with only 10 units
        Resource resource = new Resource();

        resource.setName(
                "Concurrency Test Resource"
        );

        resource.setDescription(
                "Resource used for concurrency testing"
        );

        resource.setCategory(
                ResourceCategory.values()[0]
        );

        resource.setQuantity(10);
        resource.setUnit("units");
        resource.setAvailable(true);

        resource.setLocation(
                "Test Location"
        );

        resource.setNgo(ngo);

        resource = resourceRepository.save(resource);
        resourceId = resource.getId();

        // Each transaction tries to reserve 7 units.
        // Only one reservation should succeed.
        ReservationRequestDTO requestDTO1 =
                new ReservationRequestDTO();

        requestDTO1.setRequestId(requestId);
        requestDTO1.setResourceId(resourceId);
        requestDTO1.setQuantity(7);

        ReservationRequestDTO requestDTO2 =
                new ReservationRequestDTO();

        requestDTO2.setRequestId(requestId);
        requestDTO2.setResourceId(resourceId);
        requestDTO2.setQuantity(7);

        User savedNgo = ngo;

        ExecutorService executor =
                Executors.newFixedThreadPool(2);

        CountDownLatch startLatch =
                new CountDownLatch(1);

        List<Future<ReservationResponseDTO>> futures =
                new ArrayList<>();

        try {

            // Transaction 1
            futures.add(
                    executor.submit(() -> {

                        startLatch.await();

                        return transactionTemplate.execute(
                                status ->
                                        reservationService
                                                .createReservation(
                                                        requestDTO1,
                                                        savedNgo
                                                )
                        );
                    })
            );

            // Transaction 2
            futures.add(
                    executor.submit(() -> {

                        startLatch.await();

                        return transactionTemplate.execute(
                                status ->
                                        reservationService
                                                .createReservation(
                                                        requestDTO2,
                                                        savedNgo
                                                )
                        );
                    })
            );

            // Start both threads at approximately
            // the same time.
            startLatch.countDown();

            int successCount = 0;
            int failureCount = 0;

            for (Future<ReservationResponseDTO> future :
                    futures) {

                try {

                    ReservationResponseDTO response =
                            future.get(
                                    10,
                                    TimeUnit.SECONDS
                            );

                    assertNotNull(response);

                    successCount++;

                    synchronized (reservationIds) {
                        reservationIds.add(
                                response.getId()
                        );
                    }

                } catch (ExecutionException ex) {

                    failureCount++;

                    Throwable cause =
                            ex.getCause();

                    assertTrue(
                            cause instanceof RuntimeException,
                            "Expected a business/runtime exception"
                    );
                }
            }

            // Exactly one reservation must succeed.
            assertEquals(
                    1,
                    successCount,
                    "Exactly one concurrent reservation should succeed"
            );

            // Exactly one reservation must fail.
            assertEquals(
                    1,
                    failureCount,
                    "Exactly one concurrent reservation should fail"
            );

            // Reload resource from database.
            Resource resourceAfter =
                    resourceRepository
                            .findById(resourceId)
                            .orElseThrow();

            // 10 - 7 = 3
            assertEquals(
                    3,
                    resourceAfter.getQuantity()
            );

            // Resource remains available because
            // 3 units remain.
            assertTrue(
                    resourceAfter.isAvailable()
            );

        } finally {

            executor.shutdownNow();

            assertTrue(
                    executor.awaitTermination(
                            10,
                            TimeUnit.SECONDS
                    )
            );
        }
    }
}