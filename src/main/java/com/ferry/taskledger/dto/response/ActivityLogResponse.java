package com.ferry.taskledger.dto.response;

import java.time.LocalDateTime;

public class ActivityLogResponse {

    private Long id;
    private Long organizationId;
    private Long userId;
    private String action;
    private String entityType;
    private Long entityId;
    private String description;
    private LocalDateTime createdAt;

    public ActivityLogResponse(
            Long id,
            Long organizationId,
            Long userId,
            String action,
            String entityType,
            Long entityId,
            String description,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.userId = userId;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getEntityType() {
        return entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}