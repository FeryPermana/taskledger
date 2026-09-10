package com.ferry.taskledger.mapper;

import com.ferry.taskledger.dto.response.TaskResponse;
import com.ferry.taskledger.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {

        Long assigneeId = task.getAssignee() != null
                ? task.getAssignee().getId()
                : null;

        return new TaskResponse(
                task.getId(),
                task.getProject().getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                assigneeId,
                task.getDueDate(),
                task.getCreatedBy().getId(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}