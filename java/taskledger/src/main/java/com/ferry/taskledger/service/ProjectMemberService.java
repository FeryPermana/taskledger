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

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectMemberService(
            ProjectMemberRepository projectMemberRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.projectMemberRepository = projectMemberRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectMember addMember(CreateProjectMemberRequest request) {

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() ->
                        new NoSuchElementException("Project not found")
                );

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new NoSuchElementException("User not found")
                );

        if (project.getOrganization().getStatus() == OrganizationStatus.INACTIVE) {
            throw new IllegalArgumentException("Organization is inactive");
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("User is inactive");
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

        if (!projectRepository.existsById(projectId)) {
            throw new NoSuchElementException("Project not found");
        }

        return projectMemberRepository.findMembersByProjectAndUserStatus(
                projectId,
                UserStatus.ACTIVE
        );
    }

    @Transactional

    public void removeMember(Long projectId, Long userId) {
        if (!projectRepository.existsById(projectId)) {
                throw new NoSuchElementException("Project not found");
        }

        if (!userRepository.existsById(userId)) {
                throw new NoSuchElementException("User not found");
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
}