package com.ferry.taskledger.service;

import com.ferry.taskledger.dto.request.CreateTaskRequest;
import com.ferry.taskledger.dto.request.UpdateTaskRequest;
import com.ferry.taskledger.dto.request.UpdateTaskStatusRequest;
import com.ferry.taskledger.dto.request.UpdateTaskAssigneeRequest;
import com.ferry.taskledger.entity.OrganizationStatus;
import com.ferry.taskledger.entity.Project;
import com.ferry.taskledger.entity.Task;
import com.ferry.taskledger.entity.TaskStatus;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserStatus;
import com.ferry.taskledger.repository.ProjectMemberRepository;
import com.ferry.taskledger.repository.ProjectRepository;
import com.ferry.taskledger.repository.TaskRepository;
import com.ferry.taskledger.repository.UserRepository;
import com.ferry.taskledger.service.ActivityLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ActivityLogService activityLogService;

    public TaskService(
        TaskRepository taskRepository,
        ProjectRepository projectRepository,
        UserRepository userRepository,
        ProjectMemberRepository projectMemberRepository,
        ActivityLogService activityLogService
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.activityLogService = activityLogService;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task createTask(CreateTaskRequest request) {

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                    new NoSuchElementException("Project not found")
                );
        
        if(project.getOrganization().getStatus() == OrganizationStatus.INACTIVE) {
            throw new IllegalArgumentException("Organization is inactive");
        }

        User createdBy = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() ->
                    new NoSuchElementException("User not found")        
                );

        if (createdBy.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("User is inactive");
        }

        if(!createdBy.getOrganization().getId().equals(project.getOrganization().getId())) {
            throw new IllegalArgumentException(
                "User does not belong to project organization"
            );
        }

        User assignee = null;

        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> 
                        new NoSuchElementException("Assignee not found")
                    );
            
            if (assignee.getStatus() == UserStatus.INACTIVE) {
                throw new IllegalArgumentException("Assignee is inactive");
            }

            if (!assignee.getOrganization().getId().equals(project.getOrganization().getId())) {
                throw new IllegalArgumentException(
                    "Assignee does not belong to project organization"
                );
            }

            if (!projectMemberRepository.existsByProjectIdAndUserId(
                project.getId(),
                assignee.getId()
            )) {
                throw new IllegalArgumentException(
                    "Assignee is not a member of this project"
                );
            }
        }

        Task task = new Task();

        task.setProject(project);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);
        task.setPriority(request.getPriority());
        task.setAssignee(assignee);
        task.setDueDate(request.getDueDate());
        task.setCreatedBy(createdBy);

        Task savedTask = taskRepository.save(task);

        activityLogService.createActivityLog(
                project.getOrganization().getId(),
                createdBy.getId(),
                "CREATED",
                "TASK",
                savedTask.getId(),
                "Task \"" + savedTask.getTitle() + "\" created"
        );

        return savedTask;
    }

    public Task getTaskById(Long id) {

        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );
    }

    @Transactional
    public Task updateTask(Long id, UpdateTaskRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        Task savedTask = taskRepository.save(task);

        activityLogService.createActivityLog(
                task.getProject().getOrganization().getId(),
                task.getCreatedBy().getId(),
                "UPDATED",
                "TASK",
                savedTask.getId(),
                "Task \"" + savedTask.getTitle() + "\" updated"
        );

        return savedTask;
    }

    @Transactional
    public Task updateTaskStatus(Long id, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );

        TaskStatus oldStatus = task.getStatus();

        task.setStatus(request.getStatus());

        Task savedTask = taskRepository.save(task);

        activityLogService.createActivityLog(
                task.getProject().getOrganization().getId(),
                task.getCreatedBy().getId(),
                "STATUS_CHANGED",
                "TASK",
                savedTask.getId(),
                "Task status changed from "
                        + oldStatus
                        + " to "
                        + savedTask.getStatus()
        );

        return savedTask;
    }

    @Transactional
    public Task updateTaskAssignee(Long id, UpdateTaskAssigneeRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );

        User oldAssignee = task.getAssignee();

        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() ->
                        new NoSuchElementException("Assignee not found")
                );

        if (assignee.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("Assignee is inactive");
        }

        if (!assignee.getOrganization().getId()
                .equals(task.getProject().getOrganization().getId())) {
            throw new IllegalArgumentException(
                    "Assignee does not belong to project organization"
            );
        }

        if (!projectMemberRepository.existsByProjectIdAndUserId(
                task.getProject().getId(),
                assignee.getId()
        )) {
            throw new IllegalArgumentException(
                    "Assignee is not a member of this project"
            );
        }

        task.setAssignee(assignee);

        Task savedTask = taskRepository.save(task);

        String oldAssigneeName = oldAssignee != null
                ? oldAssignee.getName()
                : "Unassigned";

        activityLogService.createActivityLog(
                task.getProject().getOrganization().getId(),
                task.getCreatedBy().getId(),
                "ASSIGNEE_CHANGED",
                "TASK",
                savedTask.getId(),
                "Task assignee changed from "
                        + oldAssigneeName
                        + " to "
                        + assignee.getName()
        );

        return savedTask;
    }

    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> 
                new NoSuchElementException("Task not found")
            );

        taskRepository.delete(task);
    }

    public List<Task> getTasksByProjectId(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new NoSuchElementException("Project not found");
        }

        return taskRepository.findByProjectId(projectId);
    }
}
