package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.response.TaskAttachmentResponse;
import com.ferry.taskledger.entity.TaskAttachment;
import com.ferry.taskledger.mapper.TaskAttachmentMapper;
import com.ferry.taskledger.service.TaskAttachmentService;
import com.ferry.taskledger.service.FileStorageService;
import com.ferry.taskledger.response.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/attachments")
public class TaskAttachmentController {

    private final TaskAttachmentService taskAttachmentService;
    private final TaskAttachmentMapper taskAttachmentMapper;
    private final FileStorageService fileStorageService;

    public TaskAttachmentController(
            TaskAttachmentService taskAttachmentService,
            TaskAttachmentMapper taskAttachmentMapper,
            FileStorageService fileStorageService) {

        this.taskAttachmentService = taskAttachmentService;
        this.taskAttachmentMapper = taskAttachmentMapper;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ApiResponse<List<TaskAttachmentResponse>> getAttachments(
            @PathVariable Long taskId) {
        List<TaskAttachment> attachments = taskAttachmentService.getAttachmentsByTaskId(taskId);

        List<TaskAttachmentResponse> response = attachments.stream()
                .map(taskAttachmentMapper::toResponse)
                .toList();

        return new ApiResponse<>(
                200,
                "Task attachments retrieved successfully",
                response);
    }

   @PostMapping
    public ApiResponse<TaskAttachmentResponse> uploadAttachment(
            @PathVariable Long taskId,
            MultipartHttpServletRequest request
    ) {
        MultipartFile file = request.getFile("file");

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        TaskAttachment attachment =
                taskAttachmentService.uploadAttachment(taskId, file);

        TaskAttachmentResponse response =
                taskAttachmentMapper.toResponse(attachment);

        return new ApiResponse<>(
                201,
                "File uploaded successfully",
                response
        );
    }

    @DeleteMapping("/{attachmentId}")
    public ApiResponse<Void> deleteAttachment(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId
    ) {
        taskAttachmentService.deleteAttachment(taskId, attachmentId);

        return new ApiResponse<>(
                200,
                "Attachment deleted successfully",
                null
        );
    }

    @GetMapping("/{attachmentId}/download")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId) {

        TaskAttachment attachment =
                taskAttachmentService.getAttachment(taskId, attachmentId);

        Resource resource =
                fileStorageService.load(attachment.getFilePath());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + attachment.getFileName() + "\""
                )
                .body(resource);
    }
}