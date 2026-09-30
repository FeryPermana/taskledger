package com.ferry.taskledger.service;

import com.ferry.taskledger.dto.request.CreateTaskRequest;
import com.ferry.taskledger.dto.request.UpdateTaskRequest;
import com.ferry.taskledger.dto.request.UpdateTaskStatusRequest;
import com.ferry.taskledger.dto.request.UpdateTaskAssigneeRequest;
import com.ferry.taskledger.entity.OrganizationStatus;
import com.ferry.taskledger.entity.Project;
import com.ferry.taskledger.entity.Task;
import com.ferry.taskledger.entity.TaskStatus;
import com.ferry.taskledger.entity.TaskPriority;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserRole;
import com.ferry.taskledger.entity.UserStatus;
import com.ferry.taskledger.repository.ProjectMemberRepository;
import com.ferry.taskledger.repository.ProjectRepository;
import com.ferry.taskledger.repository.TaskRepository;
import com.ferry.taskledger.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ferry.taskledger.specification.TaskSpecification;
import org.springframework.data.jpa.domain.Specification;

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
                        ActivityLogService activityLogService) {
                this.taskRepository = taskRepository;
                this.projectRepository = projectRepository;
                this.userRepository = userRepository;
                this.projectMemberRepository = projectMemberRepository;
                this.activityLogService = activityLogService;
        }

        private User getAuthenticatedUser() {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                String email = authentication.getName();

                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new NoSuchElementException("User not found"));
        }

        private void validateTaskAccess(
                        Task task,
                        User authenticatedUser) {

                UserRole role = authenticatedUser.getRole();

                if (role == UserRole.SUPER_ADMIN) {
                        return;
                }

                if (role == UserRole.PROJECT_MANAGER
                                || role == UserRole.TEAM_LEAD
                                || role == UserRole.MEMBER) {

                        boolean isProjectMember = projectMemberRepository.existsByProjectIdAndUserId(
                                        task.getProject().getId(),
                                        authenticatedUser.getId());

                        if (!isProjectMember) {
                                throw new AccessDeniedException(
                                                "You do not have access to this task");
                        }

                        return;
                }

                throw new AccessDeniedException(
                                "You do not have access to this task");
        }

        private List<Long> getAccessibleProjectIds(
                        User authenticatedUser) {

                UserRole role = authenticatedUser.getRole();

                if (role == UserRole.SUPER_ADMIN) {
                        return projectRepository.findAll()
                                        .stream()
                                        .map(Project::getId)
                                        .toList();
                }

                if (role == UserRole.PROJECT_MANAGER
                                || role == UserRole.TEAM_LEAD
                                || role == UserRole.MEMBER) {

                        return projectMemberRepository
                                        .findByUserId(authenticatedUser.getId())
                                        .stream()
                                        .map(projectMember -> projectMember.getProject().getId())
                                        .toList();
                }

                throw new AccessDeniedException(
                                "You do not have access to projects");
        }

        @Transactional
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER', 'TEAM_LEAD')")
        public Task createTask(CreateTaskRequest request) {

                Project project = projectRepository.findById(request.getProjectId())
                                .orElseThrow(() -> new NoSuchElementException("Project not found"));

                if (project.getOrganization().getStatus() == OrganizationStatus.INACTIVE) {

                        throw new IllegalArgumentException(
                                        "Organization is inactive");
                }

                User createdBy = getAuthenticatedUser();

                if (createdBy.getRole() != UserRole.SUPER_ADMIN) {

                        boolean isProjectMember = projectMemberRepository
                                        .existsByProjectIdAndUserId(
                                                        project.getId(),
                                                        createdBy.getId());

                        if (!isProjectMember) {
                                throw new AccessDeniedException(
                                                "You do not have access to this project");
                        }
                }

                if (createdBy.getStatus() == UserStatus.INACTIVE) {

                        throw new IllegalArgumentException(
                                        "User is inactive");
                }

                if (!createdBy.getOrganization().getId()
                                .equals(project.getOrganization().getId())) {

                        throw new IllegalArgumentException(
                                        "User does not belong to project organization");
                }

                User assignee = null;

                if (request.getAssigneeId() != null) {

                        assignee = userRepository.findById(request.getAssigneeId())
                                        .orElseThrow(() -> new NoSuchElementException(
                                                        "Assignee not found"));

                        if (assignee.getStatus() == UserStatus.INACTIVE) {

                                throw new IllegalArgumentException(
                                                "Assignee is inactive");
                        }

                        if (!assignee.getOrganization().getId()
                                        .equals(project.getOrganization().getId())) {

                                throw new IllegalArgumentException(
                                                "Assignee does not belong to project organization");
                        }

                        if (!projectMemberRepository.existsByProjectIdAndUserId(
                                        project.getId(),
                                        assignee.getId())) {

                                throw new IllegalArgumentException(
                                                "Assignee is not a member of this project");
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
                                "Task \"" + savedTask.getTitle() + "\" created");

                return savedTask;
        }

        public Task getTaskById(Long id) {

                Task task = taskRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Task not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateTaskAccess(
                                task,
                                authenticatedUser);

                return task;
        }

        @Transactional
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER', 'TEAM_LEAD', 'MEMBER')")
        public Task updateTask(
                        Long id,
                        UpdateTaskRequest request) {

                Task task = taskRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Task not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateTaskAccess(task, authenticatedUser);

                /*
                 * MEMBER hanya boleh update task
                 * yang di-assign kepadanya.
                 */
                if (authenticatedUser.getRole() == UserRole.MEMBER) {

                        if (task.getAssignee() == null
                                        || !task.getAssignee().getId()
                                                        .equals(authenticatedUser.getId())) {

                                throw new AccessDeniedException(
                                                "You can only update your own tasks");
                        }
                }

                task.setTitle(request.getTitle());
                task.setDescription(request.getDescription());
                task.setPriority(request.getPriority());
                task.setDueDate(request.getDueDate());

                Task savedTask = taskRepository.save(task);

                activityLogService.createActivityLog(
                                task.getProject().getOrganization().getId(),
                                authenticatedUser.getId(),
                                "UPDATED",
                                "TASK",
                                savedTask.getId(),
                                "Task \"" + savedTask.getTitle() + "\" updated");

                return savedTask;
        }

        @Transactional
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER', 'TEAM_LEAD', 'MEMBER')")
        public Task updateTaskStatus(
                        Long id,
                        UpdateTaskStatusRequest request) {

                Task task = taskRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Task not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateTaskAccess(task, authenticatedUser);

                /*
                 * MEMBER hanya boleh mengubah status
                 * task yang di-assign kepadanya.
                 */
                if (authenticatedUser.getRole() == UserRole.MEMBER) {

                        if (task.getAssignee() == null
                                        || !task.getAssignee().getId()
                                                        .equals(authenticatedUser.getId())) {

                                throw new AccessDeniedException(
                                                "You can only change the status of your own tasks");
                        }
                }

                TaskStatus oldStatus = task.getStatus();

                task.setStatus(request.getStatus());

                Task savedTask = taskRepository.save(task);

                activityLogService.createActivityLog(
                                task.getProject().getOrganization().getId(),
                                authenticatedUser.getId(),
                                "STATUS_CHANGED",
                                "TASK",
                                savedTask.getId(),
                                "Task status changed from "
                                                + oldStatus
                                                + " to "
                                                + savedTask.getStatus());

                return savedTask;
        }

        @Transactional
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER', 'TEAM_LEAD')")
        public Task updateTaskAssignee(
                        Long id,
                        UpdateTaskAssigneeRequest request) {

                Task task = taskRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Task not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateTaskAccess(task, authenticatedUser);

                User oldAssignee = task.getAssignee();

                User assignee = userRepository.findById(
                                request.getAssigneeId())
                                .orElseThrow(() -> new NoSuchElementException("Assignee not found"));

                if (assignee.getStatus() == UserStatus.INACTIVE) {

                        throw new IllegalArgumentException(
                                        "Assignee is inactive");
                }

                if (!assignee.getOrganization().getId()
                                .equals(task.getProject().getOrganization().getId())) {

                        throw new IllegalArgumentException(
                                        "Assignee does not belong to project organization");
                }

                if (!projectMemberRepository.existsByProjectIdAndUserId(
                                task.getProject().getId(),
                                assignee.getId())) {

                        throw new IllegalArgumentException(
                                        "Assignee is not a member of this project");
                }

                task.setAssignee(assignee);

                Task savedTask = taskRepository.save(task);

                String oldAssigneeName = oldAssignee != null
                                ? oldAssignee.getName()
                                : "Unassigned";

                activityLogService.createActivityLog(
                                task.getProject().getOrganization().getId(),
                                authenticatedUser.getId(),
                                "ASSIGNEE_CHANGED",
                                "TASK",
                                savedTask.getId(),
                                "Task assignee changed from "
                                                + oldAssigneeName
                                                + " to "
                                                + assignee.getName());

                return savedTask;
        }

        @Transactional
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER', 'TEAM_LEAD')")
        public void deleteTask(Long id) {
                Task task = taskRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Task not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateTaskAccess(task, authenticatedUser);

                Long organizationId = task.getProject().getOrganization().getId();

                Long taskId = task.getId();

                String taskTitle = task.getTitle();

                activityLogService.createActivityLog(
                                organizationId,
                                authenticatedUser.getId(),
                                "DELETED",
                                "TASK",
                                taskId,
                                "Task \"" + taskTitle + "\" deleted");

                taskRepository.delete(task);
        }

        public Page<Task> getTasksByProjectId(
                        Long projectId,
                        Pageable pageable) {

                Project project = projectRepository.findById(projectId)
                                .orElseThrow(() -> new NoSuchElementException("Project not found"));

                User authenticatedUser = getAuthenticatedUser();

                if (authenticatedUser.getRole() != UserRole.SUPER_ADMIN) {

                        boolean isProjectMember = projectMemberRepository
                                        .existsByProjectIdAndUserId(
                                                        project.getId(),
                                                        authenticatedUser.getId());

                        if (!isProjectMember) {
                                throw new AccessDeniedException(
                                                "You do not have access to this project");
                        }
                }

                return taskRepository.findByProjectId(
                                projectId,
                                pageable);
        }

        public Page<Task> getTasks(
                        String search,
                        TaskStatus status,
                        TaskPriority priority,
                        Long projectId,
                        Long assigneeId,
                        Pageable pageable) {

                User authenticatedUser = getAuthenticatedUser();

                Specification<Task> specification = Specification
                                .where(TaskSpecification.titleContains(search))
                                .and(TaskSpecification.hasStatus(status))
                                .and(TaskSpecification.hasPriority(priority))
                                .and(TaskSpecification.hasProjectId(projectId))
                                .and(TaskSpecification.hasAssigneeId(assigneeId));

                if (authenticatedUser.getRole() != UserRole.SUPER_ADMIN) {

                        List<Long> accessibleProjectIds = getAccessibleProjectIds(authenticatedUser);

                        specification = specification.and(
                                        TaskSpecification.projectIdIn(
                                                        accessibleProjectIds));
                }

                return taskRepository.findAll(
                                specification,
                                pageable);
        }
}