package com.ferry.taskledger.service;

import com.ferry.taskledger.entity.Task;
import com.ferry.taskledger.entity.TaskAttachment;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserRole;
import com.ferry.taskledger.repository.ProjectMemberRepository;
import com.ferry.taskledger.repository.TaskAttachmentRepository;
import com.ferry.taskledger.repository.TaskRepository;
import com.ferry.taskledger.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskAttachmentService {

    private final TaskAttachmentRepository taskAttachmentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final FileStorageService fileStorageService;
    private final ActivityLogService activityLogService;
    @Value("${app.file.allowed-types}")
    private List<String> allowedFileTypes;

    public TaskAttachmentService(
            TaskAttachmentRepository taskAttachmentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            ProjectMemberRepository projectMemberRepository,
            FileStorageService fileStorageService,
            ActivityLogService activityLogService
    ) {
        this.taskAttachmentRepository = taskAttachmentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.fileStorageService = fileStorageService;
        this.activityLogService = activityLogService;
    }

    public List<TaskAttachment> getAttachmentsByTaskId(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );

        User authenticatedUser = getAuthenticatedUser();

        validateTaskAccess(task, authenticatedUser);

        return taskAttachmentRepository
                .findByTaskIdOrderByCreatedAtDesc(taskId);
    }

    @Transactional
    public TaskAttachment uploadAttachment(
            Long taskId,
            MultipartFile file
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );

        User authenticatedUser = getAuthenticatedUser();

        validateTaskAccess(task, authenticatedUser);

        if (file == null
                || file.isEmpty()
                || file.getOriginalFilename() == null
                || file.getOriginalFilename().isBlank()) {

            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();

        if (contentType == null) {
            throw new IllegalArgumentException("File type is required");
        }

        if (!allowedFileTypes.contains(contentType)) {
            throw new IllegalArgumentException(
                    "File type is not allowed"
            );
        }

        String filePath = fileStorageService.store(taskId, file);

        try {
            TaskAttachment attachment = new TaskAttachment();

            attachment.setTask(task);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFilePath(filePath);
            attachment.setFileType(file.getContentType());
            attachment.setFileSize(file.getSize());
            attachment.setUploadedBy(authenticatedUser);

            TaskAttachment savedAttachment =
                    taskAttachmentRepository.save(attachment);

            activityLogService.createActivityLog(
                    task.getProject().getOrganization().getId(),
                    authenticatedUser.getId(),
                    "ATTACHMENT_UPLOADED",
                    "TASK_ATTACHMENT",
                    savedAttachment.getId(),
                    "Attachment \""
                            + savedAttachment.getFileName()
                            + "\" uploaded to task \""
                            + task.getTitle()
                            + "\""
            );

            return savedAttachment;
        } catch (Exception exception) {
            fileStorageService.delete(filePath);

            throw exception;
        }
    }

    @Transactional
    public void deleteAttachment(
            Long taskId,
            Long attachmentId
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );

        User authenticatedUser = getAuthenticatedUser();

        validateTaskAccess(task, authenticatedUser);

        TaskAttachment attachment = taskAttachmentRepository
                .findByIdAndTaskId(attachmentId, taskId)
                .orElseThrow(() ->
                        new NoSuchElementException("Attachment not found")
                );

        String fileName = attachment.getFileName();

        Long organizationId =
                task.getProject().getOrganization().getId();

        Long userId = authenticatedUser.getId();

        // Hapus file fisik
        fileStorageService.delete(
                attachment.getFilePath()
        );

        // Hapus metadata dari database
        taskAttachmentRepository.delete(attachment);

        // Simpan activity log
        activityLogService.createActivityLog(
                organizationId,
                userId,
                "ATTACHMENT_DELETED",
                "TASK_ATTACHMENT",
                attachmentId,
                "Attachment \""
                        + fileName
                        + "\" deleted from task \""
                        + task.getTitle()
                        + "\""
        );
    }

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found")
                );
    }

    private void validateTaskAccess(
            Task task,
            User authenticatedUser
    ) {

        UserRole role = authenticatedUser.getRole();

        // SUPER_ADMIN dapat mengakses semua task
        if (role == UserRole.SUPER_ADMIN) {
            return;
        }

        // PROJECT_MANAGER dan TEAM_LEAD
        // hanya dapat mengakses task dalam organization mereka
        if (role == UserRole.PROJECT_MANAGER
                || role == UserRole.TEAM_LEAD) {

            if (!task.getProject()
                    .getOrganization()
                    .getId()
                    .equals(
                            authenticatedUser
                                    .getOrganization()
                                    .getId()
                    )) {

                throw new AccessDeniedException(
                        "You do not have access to this task"
                );
            }

            return;
        }

        // MEMBER hanya dapat mengakses
        // task dari project yang dia ikuti
        if (role == UserRole.MEMBER) {

            boolean isProjectMember =
                    projectMemberRepository
                            .existsByProjectIdAndUserId(
                                    task.getProject().getId(),
                                    authenticatedUser.getId()
                            );

            if (!isProjectMember) {

                throw new AccessDeniedException(
                        "You do not have access to this task"
                );
            }

            return;
        }

        throw new AccessDeniedException(
                "You do not have access to this task"
        );
    }

    public TaskAttachment getAttachment(Long taskId, Long attachmentId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        User authenticatedUser = getAuthenticatedUser();

        validateTaskAccess(task, authenticatedUser);

        return taskAttachmentRepository
                .findByIdAndTaskId(attachmentId, taskId)
                .orElseThrow(() -> new NoSuchElementException("Attachment not found"));
    }
}