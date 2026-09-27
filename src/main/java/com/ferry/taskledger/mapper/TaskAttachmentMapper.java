package com.ferry.taskledger.mapper;

import com.ferry.taskledger.dto.response.TaskAttachmentResponse;
import com.ferry.taskledger.entity.TaskAttachment;
import org.springframework.stereotype.Component;

@Component
public class TaskAttachmentMapper {

    public TaskAttachmentResponse toResponse(TaskAttachment attachment) {
        return new TaskAttachmentResponse(
                attachment.getId(),
                attachment.getTask().getId(),
                attachment.getFileName(),
                attachment.getFileType(),
                attachment.getFileSize(),
                attachment.getUploadedBy().getId(),
                attachment.getCreatedAt(),
                 "/api/tasks/" + attachment.getTask().getId()
                + "/attachments/" + attachment.getId() + "/download"
        );
    }
}