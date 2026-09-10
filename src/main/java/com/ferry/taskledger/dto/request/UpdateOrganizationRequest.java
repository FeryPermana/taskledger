package com.ferry.taskledger.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateOrganizationRequest {
    
    @NotBlank(message = "Organization name is required")
    private String name;

    private String description;

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
}
