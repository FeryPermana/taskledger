package com.ferry.taskledger.service;

import com.ferry.taskledger.dto.request.CreateProjectRequest;
import com.ferry.taskledger.dto.request.UpdateProjectRequest;
import com.ferry.taskledger.entity.Organization;
import com.ferry.taskledger.entity.OrganizationStatus;
import com.ferry.taskledger.entity.Project;
import com.ferry.taskledger.entity.ProjectStatus;
import com.ferry.taskledger.entity.Task;
import com.ferry.taskledger.entity.TaskStatus;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserRole;
import com.ferry.taskledger.entity.UserStatus;
import com.ferry.taskledger.repository.OrganizationRepository;
import com.ferry.taskledger.repository.ProjectRepository;
import com.ferry.taskledger.repository.TaskRepository;
import com.ferry.taskledger.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ferry.taskledger.entity.ProjectMember;
import com.ferry.taskledger.repository.ProjectMemberRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectService {

        private final ProjectRepository projectRepository;
        private final OrganizationRepository organizationRepository;
        private final UserRepository userRepository;
        private final TaskRepository taskRepository;
        private final ActivityLogService activityLogService;
        private final ProjectMemberRepository projectMemberRepository;

        public ProjectService(
                        ProjectRepository projectRepository,
                        OrganizationRepository organizationRepository,
                        UserRepository userRepository,
                        TaskRepository taskRepository,
                        ActivityLogService activityLogService,
                        ProjectMemberRepository projectMemberRepository) {
                this.projectRepository = projectRepository;
                this.organizationRepository = organizationRepository;
                this.userRepository = userRepository;
                this.taskRepository = taskRepository;
                this.activityLogService = activityLogService;
                this.projectMemberRepository = projectMemberRepository;
        }

        /*
         * Mengambil user yang sedang login berdasarkan email
         * yang disimpan di SecurityContext oleh JWT filter.
         */
        private User getAuthenticatedUser() {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                String email = authentication.getName();

                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new NoSuchElementException("User not found"));
        }

        public List<Project> getAllProjects() {

                User authenticatedUser = getAuthenticatedUser();

                if (authenticatedUser.getRole() == UserRole.SUPER_ADMIN) {
                        return projectRepository.findAll();
                }

                if (authenticatedUser.getRole() == UserRole.PROJECT_MANAGER
                                || authenticatedUser.getRole() == UserRole.TEAM_LEAD
                                || authenticatedUser.getRole() == UserRole.MEMBER) {

                        return projectMemberRepository
                                        .findByUserId(authenticatedUser.getId())
                                        .stream()
                                        .map(ProjectMember::getProject)
                                        .toList();
                }

                throw new AccessDeniedException(
                                "You do not have access to projects");
        }

        /*
         * Hanya SUPER_ADMIN dan PROJECT_MANAGER
         * yang dapat membuat project.
         */
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER')")
        public Project createProject(CreateProjectRequest request) {

                Organization organization = organizationRepository
                                .findById(request.getOrganizationId())
                                .orElseThrow(() -> new NoSuchElementException("Organization not found"));

                if (organization.getStatus() == OrganizationStatus.INACTIVE) {
                        throw new IllegalArgumentException(
                                        "Organization is inactive");
                }

                /*
                 * createdBy tidak lagi berasal dari request.
                 * User pembuat project berasal dari JWT.
                 */
                User createdBy = getAuthenticatedUser();

                if (createdBy.getStatus() == UserStatus.INACTIVE) {
                        throw new IllegalArgumentException(
                                        "User is inactive");
                }

                if (createdBy.getRole() != UserRole.SUPER_ADMIN
                                && !createdBy.getOrganization().getId()
                                                .equals(organization.getId())) {

                        throw new AccessDeniedException(
                                        "You cannot create a project for another organization");
                }

                validateProjectDates(
                                request.getStartDate(),
                                request.getDueDate());

                Project project = new Project();

                project.setOrganization(organization);
                project.setName(request.getName());
                project.setDescription(request.getDescription());
                project.setStatus(ProjectStatus.PLANNING);
                project.setStartDate(request.getStartDate());
                project.setDueDate(request.getDueDate());
                project.setCreatedBy(createdBy);

                Project savedProject = projectRepository.save(project);

                if (createdBy.getRole() == UserRole.PROJECT_MANAGER) {

                        ProjectMember projectMember = new ProjectMember();

                        projectMember.setProject(savedProject);
                        projectMember.setUser(createdBy);

                        projectMemberRepository.save(projectMember);
                }

                activityLogService.createActivityLog(
                                organization.getId(),
                                createdBy.getId(),
                                "CREATED",
                                "PROJECT",
                                savedProject.getId(),
                                "Project \"" + savedProject.getName() + "\" created");

                return savedProject;
        }

        /*
         * Mengambil satu project.
         *
         * SUPER_ADMIN dapat mengakses project lintas organisasi.
         * User lain hanya boleh mengakses project organisasinya.
         */
        public Project getProjectById(Long id) {

                Project project = projectRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Project not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateProjectAccess(
                                project,
                                authenticatedUser);

                return project;
        }

        /*
         * Hanya SUPER_ADMIN dan PROJECT_MANAGER
         * yang dapat mengubah project.
         */
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER')")
        public Project updateProject(
                        Long id,
                        UpdateProjectRequest request) {

                Project project = projectRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Project not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateProjectAccess(
                                project,
                                authenticatedUser);

                validateProjectDates(
                                request.getStartDate(),
                                request.getDueDate());

                project.setName(request.getName());
                project.setDescription(request.getDescription());
                project.setStartDate(request.getStartDate());
                project.setDueDate(request.getDueDate());

                Project savedProject = projectRepository.save(project);

                activityLogService.createActivityLog(
                                project.getOrganization().getId(),
                                authenticatedUser.getId(),
                                "UPDATED",
                                "PROJECT",
                                savedProject.getId(),
                                "Project \"" + savedProject.getName() + "\" updated");

                return savedProject;
        }

        /*
         * Hanya SUPER_ADMIN dan PROJECT_MANAGER
         * yang dapat menghapus project.
         *
         * Project tidak boleh dihapus apabila
         * masih mempunyai task aktif.
         */
        @Transactional
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER')")
        public void deleteProject(Long id) {

                Project project = projectRepository.findById(id)
                                .orElseThrow(() -> new NoSuchElementException("Project not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateProjectAccess(
                                project,
                                authenticatedUser);

                List<Task> tasks = taskRepository.findByProjectId(project.getId());

                boolean hasActiveTasks = tasks.stream()
                                .anyMatch(task -> task.getStatus() != TaskStatus.DONE
                                                && task.getStatus() != TaskStatus.CANCELLED);

                if (hasActiveTasks) {
                        throw new IllegalArgumentException(
                                        "Project cannot be deleted while active tasks exist");
                }

                Long projectId = project.getId();
                String projectName = project.getName();
                Long organizationId = project.getOrganization().getId();

                activityLogService.createActivityLog(
                                organizationId,
                                authenticatedUser.getId(),
                                "DELETED",
                                "PROJECT",
                                projectId,
                                "Project \"" + projectName + "\" deleted");

                projectRepository.delete(project);
        }

        private void validateProjectAccess(
                        Project project,
                        User authenticatedUser) {

                if (authenticatedUser.getRole() == UserRole.SUPER_ADMIN) {
                        return;
                }

                if (authenticatedUser.getRole() == UserRole.PROJECT_MANAGER
                                || authenticatedUser.getRole() == UserRole.TEAM_LEAD
                                || authenticatedUser.getRole() == UserRole.MEMBER) {

                        boolean isProjectMember = projectMemberRepository.existsByProjectIdAndUserId(
                                        project.getId(),
                                        authenticatedUser.getId());

                        if (!isProjectMember) {
                                throw new AccessDeniedException(
                                                "You do not have access to this project");
                        }

                        return;
                }

                throw new AccessDeniedException(
                                "You do not have access to this project");
        }

        /*
         * Validasi tanggal project.
         */
        private void validateProjectDates(
                        java.time.LocalDate startDate,
                        java.time.LocalDate dueDate) {

                if (startDate != null
                                && dueDate != null
                                && dueDate.isBefore(startDate)) {

                        throw new IllegalArgumentException(
                                        "Due date cannot be before start date");
                }
        }
}