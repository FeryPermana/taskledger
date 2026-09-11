package com.ferry.taskledger.mapper;

import com.ferry.taskledger.dto.response.ActivityLogResponse;
import com.ferry.taskledger.entity.ActivityLog;
import org.springframework.stereotype.Component;

@Component
public class ActivityLogMapper {

    public ActivityLogResponse toResponse(ActivityLog activityLog) {

        Long userId = activityLog.getUser() != null
                ? activityLog.getUser().getId()
                : null;

        return new ActivityLogResponse(
                activityLog.getId(),
                activityLog.getOrganization().getId(),
                userId,
                activityLog.getAction(),
                activityLog.getEntityType(),
                activityLog.getEntityId(),
                activityLog.getDescription(),
                activityLog.getCreatedAt()
        );
    }
}