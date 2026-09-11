package com.ferry.taskledger.service;

import com.ferry.taskledger.entity.ActivityLog;
import com.ferry.taskledger.entity.Organization;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.repository.ActivityLogRepository;
import com.ferry.taskledger.repository.OrganizationRepository;
import com.ferry.taskledger.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public ActivityLogService(
            ActivityLogRepository activityLogRepository,
            OrganizationRepository organizationRepository,
            UserRepository userRepository
    ) {
        this.activityLogRepository = activityLogRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    public ActivityLog createActivityLog(
            Long organizationId,
            Long userId,
            String action,
            String entityType,
            Long entityId,
            String description
    ) {

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() ->
                        new NoSuchElementException("Organization not found")
                );

        User user = null;

        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new NoSuchElementException("User not found")
                    );
        }

        ActivityLog activityLog = new ActivityLog();

        activityLog.setOrganization(organization);
        activityLog.setUser(user);
        activityLog.setAction(action);
        activityLog.setEntityType(entityType);
        activityLog.setEntityId(entityId);
        activityLog.setDescription(description);

        return activityLogRepository.save(activityLog);
    }

    public List<ActivityLog> getByEntity(
            String entityType,
            Long entityId
    ) {
        return activityLogRepository.findByEntityTypeAndEntityId(
                entityType,
                entityId
        );
    }

    public List<ActivityLog> getByOrganization(
            Long organizationId
    ) {

        if (!organizationRepository.existsById(organizationId)) {
            throw new NoSuchElementException("Organization not found");
        }

        return activityLogRepository.findByOrganizationId(
                organizationId
        );
    }

    public List<ActivityLog> getByUser(
            Long userId
    ) {

        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User not found");
        }

        return activityLogRepository.findByUserId(userId);
    }
}