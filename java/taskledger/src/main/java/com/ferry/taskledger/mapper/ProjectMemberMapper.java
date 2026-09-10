package com.ferry.taskledger.mapper;

import com.ferry.taskledger.dto.response.ProjectMemberResponse;
import com.ferry.taskledger.entity.ProjectMember;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {

    public ProjectMemberResponse toResponse(ProjectMember projectMember) {

        return new ProjectMemberResponse(
                projectMember.getId(),
                projectMember.getProject().getId(),
                projectMember.getUser().getId(),
                projectMember.getCreatedAt()
        );
    }
}