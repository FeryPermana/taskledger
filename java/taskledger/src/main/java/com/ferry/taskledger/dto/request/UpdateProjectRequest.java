package com.ferry.taskledger.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class UpdateProjectRequest {

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    @FutureOrPresent (message = "Start date cannot be in the past")
    private LocalDate startDate;

    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;

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

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}