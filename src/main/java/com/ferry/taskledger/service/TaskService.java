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

                /*
                 * SUPER_ADMIN dapat mengakses
                 * seluruh task di semua organization.
                 */
                if (role == UserRole.SUPER_ADMIN) {
                        return;
                }

                /*
                 * PROJECT_MANAGER dan TEAM_LEAD hanya boleh
                 * mengakses task dari organization mereka sendiri.
                 */
                if (role == UserRole.PROJECT_MANAGER
                                || role == UserRole.TEAM_LEAD) {

                        if (!task.getProject().getOrganization().getId()
                                        .equals(authenticatedUser.getOrganization().getId())) {

                                throw new AccessDeniedException(
                                                "You do not have access to this task");
                        }

                        return;
                }

                /*
                 * MEMBER hanya boleh mengakses task
                 * dari project yang dia ikuti.
                 */
                if (role == UserRole.MEMBER) {

                        boolean isProjectMember = projectMemberRepository.existsByProjectIdAndUserId(
                                        task.getProject().getId(),
                                        authenticatedUser.getId());

                        if (!isProjectMember) {
                                throw new AccessDeniedException(
                                                "You do not have access to this task");
                        }

                        return;
                }

                /*
                 * Role lain tidak memiliki akses ke task.
                 */
                throw new AccessDeniedException(
                                "You do not have access to this task");
        }

        public List<Task> getAllTasks() {

                User authenticatedUser = getAuthenticatedUser();

                UserRole role = authenticatedUser.getRole();

                /*
                 * SUPER_ADMIN dapat melihat seluruh task
                 * dari semua organization.
                 */
                if (role == UserRole.SUPER_ADMIN) {
                        return taskRepository.findAll();
                }

                /*
                 * PROJECT_MANAGER dan TEAM_LEAD hanya melihat
                 * task dari project dalam organization mereka.
                 */
                if (role == UserRole.PROJECT_MANAGER
                                || role == UserRole.TEAM_LEAD) {

                        Long organizationId = authenticatedUser.getOrganization().getId();

                        List<Long> projectIds = projectRepository
                                        .findByOrganizationId(organizationId)
                                        .stream()
                                        .map(Project::getId)
                                        .toList();

                        if (projectIds.isEmpty()) {
                                return List.of();
                        }

                        return taskRepository.findByProjectIdIn(projectIds);
                }

                /*
                 * MEMBER hanya melihat task dari
                 * project yang dia ikuti.
                 */
                if (role == UserRole.MEMBER) {

                        List<Long> projectIds = projectMemberRepository
                                        .findByUserId(authenticatedUser.getId())
                                        .stream()
                                        .map(projectMember -> projectMember.getProject().getId())
                                        .toList();

                        if (projectIds.isEmpty()) {
                                return List.of();
                        }

                        return taskRepository.findByProjectIdIn(projectIds);
                }

                throw new AccessDeniedException(
                                "You do not have access to tasks");
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

        public List<Task> getTasksByProjectId(Long projectId) {
                Project project = projectRepository.findById(projectId)
                                .orElseThrow(() -> new NoSuchElementException("Project not found"));

                User authenticatedUser = getAuthenticatedUser();

                /*
                 * SUPER_ADMIN dapat melihat task
                 * dari project di organization mana pun.
                 */
                if (authenticatedUser.getRole() == UserRole.SUPER_ADMIN) {
                        return taskRepository.findByProjectId(projectId);
                }

                /*
                 * PROJECT_MANAGER dan TEAM_LEAD hanya boleh
                 * melihat task dari organization mereka sendiri.
                 */
                if (authenticatedUser.getRole() == UserRole.PROJECT_MANAGER
                                || authenticatedUser.getRole() == UserRole.TEAM_LEAD) {

                        if (!project.getOrganization().getId()
                                        .equals(authenticatedUser.getOrganization().getId())) {

                                throw new AccessDeniedException(
                                                "You do not have access to this project");
                        }

                        return taskRepository.findByProjectId(projectId);
                }

                /*
                 * MEMBER hanya boleh melihat task
                 * dari project yang dia ikuti.
                 */
                boolean isProjectMember = projectMemberRepository.existsByProjectIdAndUserId(
                                project.getId(),
                                authenticatedUser.getId());

                if (!isProjectMember) {
                        throw new AccessDeniedException(
                                        "You do not have access to this project");
                }

                return taskRepository.findByProjectId(projectId);
        }
}