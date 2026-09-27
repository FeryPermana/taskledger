package com.ferry.taskledger.dto.response;

import java.time.LocalDateTime;

public record TaskAttachmentResponse(
        Long id,
        Long taskId,
        String fileName,
        String fileType,
        Long fileSize,
        Long uploadedById,
        LocalDateTime createdAt,
        String fileUrl
) {}