package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.request.CreateProjectMemberRequest;
import com.ferry.taskledger.dto.response.ProjectMemberResponse;
import com.ferry.taskledger.entity.ProjectMember;
import com.ferry.taskledger.mapper.ProjectMemberMapper;
import com.ferry.taskledger.response.ApiResponse;
import com.ferry.taskledger.service.ProjectMemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;
    private final ProjectMemberMapper projectMemberMapper;

    public ProjectMemberController(
            ProjectMemberService projectMemberService,
            ProjectMemberMapper projectMemberMapper
    ) {
        this.projectMemberService = projectMemberService;
        this.projectMemberMapper = projectMemberMapper;
    }

    @PostMapping
    public ApiResponse<ProjectMemberResponse> addMember(
            @Valid @RequestBody CreateProjectMemberRequest request
    ) {

        ProjectMember projectMember =
                projectMemberService.addMember(request);

        ProjectMemberResponse response =
                projectMemberMapper.toResponse(projectMember);

        return new ApiResponse<>(
                201,
                "Project member added successfully",
                response
        );
    }

    @GetMapping("/project/{projectId}")
    public ApiResponse<List<ProjectMemberResponse>> getMembersByProject(
            @PathVariable Long projectId
    ) {

        List<ProjectMember> members =
                projectMemberService.getMembersByProjectId(projectId);

        List<ProjectMemberResponse> responses = members.stream()
                .map(projectMemberMapper::toResponse)
                .toList();

        return new ApiResponse<>(
                200,
                "Project members retrieved successfully",
                responses
        );
    }

    @DeleteMapping("/project/{projectId}/user/{userId}")
    public ApiResponse<Void> removeMember(
        @PathVariable Long projectId,
        @PathVariable Long userId
    ) {
        projectMemberService.removeMember(projectId, userId);

        return new ApiResponse<>(
            200,
            "Project member removed successfully",
            null
        );
    }
}