package com.disasterconnect.service;

import com.disasterconnect.dto.RequestHistoryResponseDTO;
import com.disasterconnect.entity.Request;
import com.disasterconnect.entity.RequestHistory;
import com.disasterconnect.entity.User;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.repository.RequestHistoryRepository;
import com.disasterconnect.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestHistoryService {

    private final RequestHistoryRepository requestHistoryRepository;
    private final RequestRepository requestRepository;

    public RequestHistoryService(
            RequestHistoryRepository requestHistoryRepository,
            RequestRepository requestRepository) {

        this.requestHistoryRepository = requestHistoryRepository;
        this.requestRepository = requestRepository;
    }

    public RequestHistoryResponseDTO createHistory(
            Long requestId,
            RequestStatus oldStatus,
            RequestStatus newStatus,
            User changedBy) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Request not found with id: " + requestId
                        )
                );

        RequestHistory history = new RequestHistory();

        history.setRequest(request);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setChangedAt(LocalDateTime.now());
        history.setChangedBy(changedBy);

        RequestHistory savedHistory =
                requestHistoryRepository.save(history);

        return convertToResponseDTO(savedHistory);
    }

    public List<RequestHistoryResponseDTO> getHistory(
            Long requestId) {

        if (!requestRepository.existsById(requestId)) {
            throw new RuntimeException(
                    "Request not found with id: " + requestId
            );
        }

        return requestHistoryRepository
                .findByRequestIdOrderByChangedAtAsc(requestId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    private RequestHistoryResponseDTO convertToResponseDTO(
            RequestHistory history) {

        Long changedById = history.getChangedBy() != null
                ? history.getChangedBy().getId()
                : null;

        return new RequestHistoryResponseDTO(
                history.getId(),
                history.getRequest().getId(),
                history.getOldStatus(),
                history.getNewStatus(),
                history.getChangedAt(),
                changedById
        );
    }
}