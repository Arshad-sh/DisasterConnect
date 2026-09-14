package com.disasterconnect.service;

import com.disasterconnect.dto.ReservationRequestDTO;
import com.disasterconnect.dto.ReservationResponseDTO;
import com.disasterconnect.entity.Request;
import com.disasterconnect.entity.Reservation;
import com.disasterconnect.entity.Resource;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.ReservationStatus;
import com.disasterconnect.exception.BusinessRuleException;
import com.disasterconnect.exception.ResourceNotFoundException;
import com.disasterconnect.repository.RequestRepository;
import com.disasterconnect.repository.ReservationRepository;
import com.disasterconnect.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private ReservationService reservationService;

    private User ngo;
    private Request request;
    private Resource resource;
    private ReservationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        ngo = mock(User.class);

        request = new Request();
        request.setId(10L);

        resource = new Resource();
        resource.setId(20L);
        resource.setName("Emergency Medicine");
        resource.setQuantity(10);
        resource.setAvailable(true);
        resource.setNgo(ngo);

        requestDTO = new ReservationRequestDTO();
        requestDTO.setRequestId(10L);
        requestDTO.setResourceId(20L);
        requestDTO.setQuantity(4);
    }

    @Test
    void createReservation_shouldReduceResourceQuantityAndCreateReservation() {

        when(ngo.getId()).thenReturn(1L);

        when(requestRepository.findById(10L))
                .thenReturn(Optional.of(request));

        when(resourceRepository.findByIdForUpdate(20L))
                .thenReturn(Optional.of(resource));

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        ReservationResponseDTO response =
                reservationService.createReservation(
                        requestDTO,
                        ngo
                );

        assertNotNull(response);

        assertEquals(
                10L,
                response.getRequestId()
        );

        assertEquals(
                20L,
                response.getResourceId()
        );

        assertEquals(
                4,
                response.getQuantity()
        );

        assertEquals(
                ReservationStatus.PENDING,
                response.getStatus()
        );

        assertEquals(
                6,
                resource.getQuantity()
        );

        assertTrue(
                resource.isAvailable()
        );

        verify(resourceRepository)
                .save(resource);

        verify(reservationRepository)
                .save(any(Reservation.class));
    }

    @Test
    void createReservation_shouldSaveReservationWithCorrectQuantityAndStatus() {

        when(ngo.getId()).thenReturn(1L);

        when(requestRepository.findById(10L))
                .thenReturn(Optional.of(request));

        when(resourceRepository.findByIdForUpdate(20L))
                .thenReturn(Optional.of(resource));

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        reservationService.createReservation(
                requestDTO,
                ngo
        );

        ArgumentCaptor<Reservation> reservationCaptor =
                ArgumentCaptor.forClass(Reservation.class);

        verify(reservationRepository)
                .save(reservationCaptor.capture());

        Reservation savedReservation =
                reservationCaptor.getValue();

        assertEquals(
                request,
                savedReservation.getRequest()
        );

        assertEquals(
                resource,
                savedReservation.getResource()
        );

        assertEquals(
                4,
                savedReservation.getQuantity()
        );

        assertEquals(
                ReservationStatus.PENDING,
                savedReservation.getStatus()
        );

        assertNotNull(
                savedReservation.getCreatedAt()
        );
    }

    @Test
    void createReservation_shouldThrowExceptionWhenRequestDoesNotExist() {

        when(requestRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.createReservation(
                        requestDTO,
                        ngo
                )
        );

        verify(resourceRepository, never())
                .findByIdForUpdate(anyLong());

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    @Test
    void createReservation_shouldThrowExceptionWhenQuantityExceedsAvailableResource() {

        when(ngo.getId()).thenReturn(1L);

        when(requestRepository.findById(10L))
                .thenReturn(Optional.of(request));

        when(resourceRepository.findByIdForUpdate(20L))
                .thenReturn(Optional.of(resource));

        requestDTO.setQuantity(11);

        assertThrows(
                BusinessRuleException.class,
                () -> reservationService.createReservation(
                        requestDTO,
                        ngo
                )
        );

        assertEquals(
                10,
                resource.getQuantity()
        );

        verify(resourceRepository, never())
                .save(any(Resource.class));

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    @Test
    void createReservation_shouldThrowExceptionWhenNgoDoesNotOwnResource() {

        when(ngo.getId()).thenReturn(1L);

        User resourceOwner = mock(User.class);

        when(resourceOwner.getId()).thenReturn(2L);

        resource.setNgo(resourceOwner);

        when(requestRepository.findById(10L))
                .thenReturn(Optional.of(request));

        when(resourceRepository.findByIdForUpdate(20L))
                .thenReturn(Optional.of(resource));

        assertThrows(
                BusinessRuleException.class,
                () -> reservationService.createReservation(
                        requestDTO,
                        ngo
                )
        );

        verify(resourceRepository, never())
                .save(any(Resource.class));

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    @Test
    void createReservation_shouldThrowExceptionWhenResourceIsUnavailable() {

        when(ngo.getId()).thenReturn(1L);

        when(requestRepository.findById(10L))
                .thenReturn(Optional.of(request));

        resource.setAvailable(false);

        when(resourceRepository.findByIdForUpdate(20L))
                .thenReturn(Optional.of(resource));

        assertThrows(
                BusinessRuleException.class,
                () -> reservationService.createReservation(
                        requestDTO,
                        ngo
                )
        );

        verify(resourceRepository, never())
                .save(any(Resource.class));

        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }
}