package com.ferry.taskledger.dto.request;

import com.ferry.taskledger.entity.OrganizationStatus;
import jakarta.validation.constraints.NotBlank;

public class CreateOrganizationRequest {
    @NotBlank(message = "Organization name is required")
    private String name;

    private String description;
    private OrganizationStatus status;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrganizationStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizationStatus status) {
        this.status = status;
    }
}
