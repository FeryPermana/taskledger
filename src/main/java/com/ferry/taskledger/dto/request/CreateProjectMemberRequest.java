package com.ferry.taskledger.dto.request;

import jakarta.validation.constraints.NotNull;

public class CreateProjectMemberRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}