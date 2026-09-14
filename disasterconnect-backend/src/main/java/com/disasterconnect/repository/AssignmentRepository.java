package com.disasterconnect.repository;

import com.disasterconnect.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByVolunteerIdOrderByAssignedAtDesc(Long volunteerId);
}
