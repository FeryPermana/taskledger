package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.request.CreateProjectRequest;
import com.ferry.taskledger.dto.request.UpdateProjectRequest;
import com.ferry.taskledger.dto.response.ProjectResponse;
import com.ferry.taskledger.entity.Project;
import com.ferry.taskledger.mapper.ProjectMapper;
import com.ferry.taskledger.response.ApiResponse;
import com.ferry.taskledger.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    public ProjectController(
        ProjectService projectService,
        ProjectMapper projectMapper
    ) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
    }

    @GetMapping
    public ApiResponse<List<ProjectResponse>> getAllProjects() {
        
        List<Project> projects = projectService.getAllProjects();

        List<ProjectResponse> responses = projects.stream()
                .map(projectMapper::toResponse)
                .toList();
        
        return new ApiResponse<>(
            200,
            "Projects retrieved successfully",
            responses  
        );
    }

    @PostMapping
    public ApiResponse<ProjectResponse> createProject(
        @Valid @RequestBody CreateProjectRequest request
    ) {

        Project project = projectService.createProject(request);

        ProjectResponse response = projectMapper.toResponse(project);

        return new ApiResponse<>(
            201,
            "Project created successfully",
            response
        );
    }

    @GetMapping("{id}")
    public ApiResponse<ProjectResponse> getProjectById(
        @PathVariable Long id
    ) {
        Project project = projectService.getProjectById(id);

        ProjectResponse response = projectMapper.toResponse(project);

        return new ApiResponse<>(
            200,
            "Project retrieved successfully",
            response
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProjectRequest request
    ) {

        Project project = projectService.updateProject(id, request);

        ProjectResponse response = projectMapper.toResponse(project);

        return new ApiResponse<>(
                200,
                "Project updated successfully",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProject(
            @PathVariable Long id
    ) {

        projectService.deleteProject(id);

        return new ApiResponse<>(
                200,
                "Project deleted successfully",
                null
        );
    }
}
