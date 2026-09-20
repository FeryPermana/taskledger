package com.ferry.taskledger.service;

import com.ferry.taskledger.dto.request.CreateProjectMemberRequest;
import com.ferry.taskledger.entity.OrganizationStatus;
import com.ferry.taskledger.entity.Project;
import com.ferry.taskledger.entity.ProjectMember;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserStatus;
import com.ferry.taskledger.repository.ProjectMemberRepository;
import com.ferry.taskledger.repository.ProjectRepository;
import com.ferry.taskledger.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ferry.taskledger.entity.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectMemberService {

        private final ProjectMemberRepository projectMemberRepository;
        private final ProjectRepository projectRepository;
        private final UserRepository userRepository;

        private User getAuthenticatedUser() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                String email = authentication.getName();

                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new NoSuchElementException("User not found"));
        }

        public ProjectMemberService(
                        ProjectMemberRepository projectMemberRepository,
                        ProjectRepository projectRepository,
                        UserRepository userRepository) {
                this.projectMemberRepository = projectMemberRepository;
                this.projectRepository = projectRepository;
                this.userRepository = userRepository;
        }

        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER')")
        public ProjectMember addMember(CreateProjectMemberRequest request) {

                Project project = projectRepository.findById(request.getProjectId())
                        .orElseThrow(() ->
                                new NoSuchElementException("Project not found")
                        );

                User authenticatedUser = getAuthenticatedUser();

                validateProjectAccess(project, authenticatedUser);

                User user = userRepository.findById(request.getUserId())
                        .orElseThrow(() ->
                                new NoSuchElementException("User not found")
                        );

                if (project.getOrganization().getStatus()
                        == OrganizationStatus.INACTIVE) {

                        throw new IllegalArgumentException(
                                "Organization is inactive"
                        );
                }

                if (user.getStatus() == UserStatus.INACTIVE) {

                        throw new IllegalArgumentException(
                                "User is inactive"
                        );
                }

                if (!user.getOrganization().getId()
                        .equals(project.getOrganization().getId())) {

                        throw new IllegalArgumentException(
                                "User does not belong to project organization"
                        );
                }

                if (projectMemberRepository.existsByProjectIdAndUserId(
                        project.getId(),
                        user.getId()
                )) {

                        throw new IllegalArgumentException(
                                "User is already a member of this project"
                        );
                }

                ProjectMember projectMember = new ProjectMember();

                projectMember.setProject(project);
                projectMember.setUser(user);

                return projectMemberRepository.save(projectMember);
        }

        public List<ProjectMember> getMembersByProjectId(Long projectId) {
                Project project = projectRepository.findById(projectId)
                        .orElseThrow(() ->
                                new NoSuchElementException("Project not found")
                        );

                User authenticatedUser = getAuthenticatedUser();

                validateProjectAccess(project, authenticatedUser);

                return projectMemberRepository.findMembersByProjectAndUserStatus(
                        projectId,
                        UserStatus.ACTIVE
                );
        }

        @Transactional
        @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'PROJECT_MANAGER')")
                public void removeMember(Long projectId, Long userId) {

                Project project = projectRepository.findById(projectId)
                        .orElseThrow(() ->
                                new NoSuchElementException("Project not found")
                        );

                User authenticatedUser = getAuthenticatedUser();

                validateProjectAccess(project, authenticatedUser);

                if (!userRepository.existsById(userId)) {

                        throw new NoSuchElementException(
                                "User not found"
                        );
                }

                if (!projectMemberRepository.existsByProjectIdAndUserId(
                        projectId,
                        userId
                )) {

                        throw new NoSuchElementException(
                                "User is not a member of this project"
                        );
                }

                projectMemberRepository.deleteByProjectIdAndUserId(
                        projectId,
                        userId
                );
        }

        private void validateProjectAccess(
                Project project,
                User authenticatedUser
        ) {
                if (authenticatedUser.getRole() == UserRole.SUPER_ADMIN) {
                        return;
                }

                if (!project.getOrganization().getId()
                        .equals(authenticatedUser.getOrganization().getId())) {

                        throw new AccessDeniedException(
                                "You do not have access to this project"
                        );
                }
        }
}