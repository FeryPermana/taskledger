package com.ferry.taskledger.service;

import com.ferry.taskledger.dto.request.CreateProjectRequest;
import com.ferry.taskledger.dto.request.UpdateProjectRequest;
import com.ferry.taskledger.entity.Organization;
import com.ferry.taskledger.entity.OrganizationStatus;
import com.ferry.taskledger.entity.Project;
import com.ferry.taskledger.entity.ProjectStatus;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserStatus;
import com.ferry.taskledger.repository.OrganizationRepository;
import com.ferry.taskledger.repository.ProjectRepository;
import com.ferry.taskledger.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            OrganizationRepository organizationRepository,
            UserRepository userRepository
    ) {
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(CreateProjectRequest request) {
        Organization organization = organizationRepository.findById(
                request.getOrganizationId()
        ).orElseThrow(() ->
                new NoSuchElementException("Organization not found")
        );

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            throw new IllegalArgumentException("Organization is inactive");
        }

        User createdBy = userRepository.findById(request.getCreatedBy())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (createdBy.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("User is inactive");
        }

        if (!createdBy.getOrganization().getId().equals(organization.getId())) {
            throw new IllegalArgumentException("User does not belong to the organization");
        }

        if (request.getDueDate() != null
                && request.getStartDate() != null
                && request.getDueDate().isBefore(request.getStartDate())) {

            throw new IllegalArgumentException(
                    "Due date cannot be before start date"
            );
        }

        Project project = new Project();
        
        project.setOrganization(organization);
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStatus(ProjectStatus.PLANNING);
        project.setStartDate(request.getStartDate());
        project.setDueDate(request.getDueDate());
        project.setCreatedBy(createdBy);

        return projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> 
                    new NoSuchElementException("Project not found")
            );
    }

    public Project updateProject(Long id, UpdateProjectRequest request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> 
                    new NoSuchElementException("Project not found")
                );

        if(request.getDueDate() != null
                && request.getStartDate() != null
                && request.getDueDate().isBefore(request.getStartDate())) {

            throw new IllegalArgumentException(
                    "Due date cannot be before start date"
            );
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setDueDate(request.getDueDate());

        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> 
                    new NoSuchElementException("Project not found")
                );

        projectRepository.delete(project);
    }
}
