package com.ferry.taskledger.repository;

import com.ferry.taskledger.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findByEntityTypeAndEntityId(
            String entityType,
            Long entityId
    );

    List<ActivityLog> findByOrganizationId(
            Long organizationId
    );

    List<ActivityLog> findByUserId(
            Long userId
    );
}