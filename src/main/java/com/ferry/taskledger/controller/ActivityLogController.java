package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.response.ActivityLogResponse;
import com.ferry.taskledger.response.ApiResponse;
import com.ferry.taskledger.entity.ActivityLog;
import com.ferry.taskledger.mapper.ActivityLogMapper;
import com.ferry.taskledger.service.ActivityLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
public class ActivityLogController {

    private final ActivityLogService activityLogService;
    private final ActivityLogMapper activityLogMapper;

    public ActivityLogController(
            ActivityLogService activityLogService,
            ActivityLogMapper activityLogMapper
    ) {
        this.activityLogService = activityLogService;
        this.activityLogMapper = activityLogMapper;
    }

    @GetMapping("/entity")
    public ApiResponse<List<ActivityLogResponse>> getByEntity(
            @RequestParam String entityType,
            @RequestParam Long entityId
    ) {
        List<ActivityLog> logs =
                activityLogService.getByEntity(entityType, entityId);

        List<ActivityLogResponse> response = logs.stream()
                .map(activityLogMapper::toResponse)
                .toList();

        return new ApiResponse<>(
                200,
                "Activity logs retrieved successfully",
                response
        );
    }

    @GetMapping("/organization/{organizationId}")
    public ApiResponse<List<ActivityLogResponse>> getByOrganization(
            @PathVariable Long organizationId
    ) {
        List<ActivityLog> logs =
                activityLogService.getByOrganization(organizationId);

        List<ActivityLogResponse> response = logs.stream()
                .map(activityLogMapper::toResponse)
                .toList();

        return new ApiResponse<>(
                200,
                "Activity logs retrieved successfully",
                response
        );
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<ActivityLogResponse>> getByUser(
            @PathVariable Long userId
    ) {
        List<ActivityLog> logs =
                activityLogService.getByUser(userId);

        List<ActivityLogResponse> response = logs.stream()
                .map(activityLogMapper::toResponse)
                .toList();

        return new ApiResponse<>(
                200,
                "Activity logs retrieved successfully",
                response
        );
    }
}