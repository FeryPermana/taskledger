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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public TaskService(
        TaskRepository taskRepository,
        ProjectRepository projectRepository,
        UserRepository userRepository,
        ProjectMemberRepository projectMemberRepository
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

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

        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {

        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );
    }

    public Task updateTask(Long id, UpdateTaskRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> 
                    new NoSuchElementException("Task not found")
                );
        
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Long id, UpdateTaskStatusRequest request) {

        Task task = taskRepository.findById(id)
            .orElseThrow(() -> 
                new NoSuchElementException("Task not found")
            );

        task.setStatus(request.getStatus());

        return taskRepository.save(task);
    }

    public Task updateTaskAssignee(Long id, UpdateTaskAssigneeRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Task not found")
                );

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

        return taskRepository.save(task);
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
