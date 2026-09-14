package com.disasterconnect.repository;

import com.disasterconnect.entity.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestHistoryRepository
        extends JpaRepository<RequestHistory, Long> {

    List<RequestHistory> findByRequestIdOrderByChangedAtAsc(Long requestId);
}