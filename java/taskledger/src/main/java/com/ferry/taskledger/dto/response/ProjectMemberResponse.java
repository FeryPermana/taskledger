package com.ferry.taskledger.dto.response;

import java.time.LocalDateTime;

public class ProjectMemberResponse {

    private Long id;
    private Long projectId;
    private Long userId;
    private LocalDateTime createdAt;

    public ProjectMemberResponse(
            Long id,
            Long projectId,
            Long userId,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}