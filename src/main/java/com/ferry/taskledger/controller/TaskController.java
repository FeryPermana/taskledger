package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.request.CreateTaskRequest;
import com.ferry.taskledger.dto.request.UpdateTaskRequest;
import com.ferry.taskledger.dto.request.UpdateTaskStatusRequest;
import com.ferry.taskledger.dto.request.UpdateTaskAssigneeRequest;
import com.ferry.taskledger.dto.response.TaskResponse;
import com.ferry.taskledger.entity.Task;
import com.ferry.taskledger.mapper.TaskMapper;
import com.ferry.taskledger.response.ApiResponse;
import com.ferry.taskledger.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

        private final TaskService taskService;
        private final TaskMapper taskMapper;

        public TaskController(
                TaskService taskService,
                TaskMapper taskMapper
        ) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        }

        @GetMapping
        public ApiResponse<List<TaskResponse>> getAllTasks() {

        List<Task> tasks = taskService.getAllTasks();

        List<TaskResponse> responses = tasks.stream()
                .map(taskMapper::toResponse)
                .toList();

        return new ApiResponse<>(
                200,
                "Tasks retrieved successfully",
                responses
        );
        }

        @PostMapping
        public ApiResponse<TaskResponse> createTask(
                @Valid @RequestBody CreateTaskRequest request
        ) {

        Task task = taskService.createTask(request);

        TaskResponse response = taskMapper.toResponse(task);

        return new ApiResponse<>(
                201,
                "Task created successfully",
                response
        );
        }

        @GetMapping("/{id}")
        public ApiResponse<TaskResponse> getTaskById(
                @PathVariable Long id
        ) {

        Task task = taskService.getTaskById(id);

        TaskResponse response = taskMapper.toResponse(task);

        return new ApiResponse<>(
                200,
                "Task retrieved successfully",
                response
        );
        }

        @PutMapping("/{id}")
        public ApiResponse<TaskResponse> updateTask(
                @PathVariable Long id,
                @Valid @RequestBody UpdateTaskRequest request
        ) {

        Task task = taskService.updateTask(id, request);

        TaskResponse response = taskMapper.toResponse(task);

        return new ApiResponse<>(
                200,
                "Task updated successfully",
                response
        );
        }

        @PatchMapping("/{id}/status")
        public ApiResponse<TaskResponse> updateTaskStatus(
                @PathVariable Long id,
                @Valid @RequestBody UpdateTaskStatusRequest request
        ) {

                Task task = taskService.updateTaskStatus(id, request);

                TaskResponse response = taskMapper.toResponse(task);

                return new ApiResponse<>(
                        200,
                        "Task status updated successfully",
                        response
                );
        }

        @PatchMapping("/{id}/assignee")
        public ApiResponse<TaskResponse> updateTaskAssignee(
                @PathVariable Long id,
                @Valid @RequestBody UpdateTaskAssigneeRequest request
        ) {
                Task task = taskService.updateTaskAssignee(id, request);

                TaskResponse response = taskMapper.toResponse(task);

                return new ApiResponse<>(
                        200,
                        "Task assignee updated successfully",
                        response
                );
        }

        @DeleteMapping("/{id}")
        public ApiResponse<Void> deleteTask(@PathVariable Long id) {

                taskService.deleteTask(id);

                return new ApiResponse<>(
                        200,
                        "Task deleted successfully",
                        null
                );
        }

        @GetMapping("/project/{projectId}")
        public ApiResponse<List<TaskResponse>> getTasksByProject(
                @PathVariable Long projectId
        ) {
                List<Task> tasks = taskService.getTasksByProjectId(projectId);

                List<TaskResponse> responses = tasks.stream()
                        .map(taskMapper::toResponse)
                        .toList();

                return new ApiResponse<>(
                        200,
                        "Tasks retrieved successfully",
                        responses
                );
        }
}