package com.disasterconnect.repository;

import com.disasterconnect.entity.Request;
import com.disasterconnect.enums.RequestStatus;
import com.disasterconnect.enums.Urgency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("""
            SELECT r FROM Request r
            WHERE (:search IS NULL OR
                   LOWER(r.title) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(r.location) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<Request> searchRequests(
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
            SELECT r FROM Request r
            WHERE (:status IS NULL OR r.status = :status)
              AND (:urgency IS NULL OR r.urgency = :urgency)
            """)
    Page<Request> filterRequests(
            @Param("status") RequestStatus status,
            @Param("urgency") Urgency urgency,
            Pageable pageable
    );

    @Query("""
            SELECT r FROM Request r
            WHERE (:search IS NULL OR
                   LOWER(r.title) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(r.location) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:status IS NULL OR r.status = :status)
              AND (:urgency IS NULL OR r.urgency = :urgency)
            """)
    Page<Request> searchAndFilterRequests(
            @Param("search") String search,
            @Param("status") RequestStatus status,
            @Param("urgency") Urgency urgency,
            Pageable pageable
    );

    Page<Request> findByUserId(
            Long userId,
            Pageable pageable
    );
}