package com.ferry.taskledger.dto.request;

import jakarta.validation.constraints.NotNull;

public class UpdateTaskAssigneeRequest {

    @NotNull(message = "Assignee ID is required")
    private Long assigneeId;

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }
}