package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.request.CreateUserRequest;
import com.ferry.taskledger.dto.request.UpdateUserRequest;
import com.ferry.taskledger.dto.response.UserResponse;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.mapper.UserMapper;
import com.ferry.taskledger.response.ApiResponse;
import com.ferry.taskledger.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

        private final UserService userService;
        private final UserMapper userMapper;

        public UserController(
                        UserService userService,
                        UserMapper userMapper) {
                this.userService = userService;
                this.userMapper = userMapper;
        }

        @GetMapping
        public ApiResponse<List<UserResponse>> getAllUsers() {

                List<User> users = userService.getAllUsers();

                List<UserResponse> responses = users.stream()
                                .map(userMapper::toResponse)
                                .toList();

                return new ApiResponse<>(
                                200,
                                "Users retrieved successfully",
                                responses);
        }

        @PostMapping
        public ApiResponse<UserResponse> createUser(
                        @Valid @RequestBody CreateUserRequest request) {

                User user = userService.createUser(request);

                UserResponse response = userMapper.toResponse(user);

                return new ApiResponse<>(
                                201,
                                "User created successfully",
                                response);
        }

        @GetMapping("/{id}")
        public ApiResponse<UserResponse> getUserById(
                        @PathVariable Long id) {

                User user = userService.getUserById(id);

                UserResponse response = userMapper.toResponse(user);

                return new ApiResponse<>(
                                200,
                                "User retrieved successfully",
                                response);
        }

        @PutMapping("/{id}")
        public ApiResponse<UserResponse> updateUser(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateUserRequest request) {
                User user = userService.updateUser(id, request);

                UserResponse response = userMapper.toResponse(user);

                return new ApiResponse<>(
                                200,
                                "User updated successfully",
                                response);
        }

        @DeleteMapping("/{id}")
        public ApiResponse<Void> deactivateUser(
                        @PathVariable Long id) {
                userService.deactivateUser(id);
                return new ApiResponse<>(
                                200,
                                "User deactivated successfully",
                                null);
        }

        @PatchMapping("/{id}/activate")
        public ApiResponse<Void> activateUser(
                        @PathVariable Long id) {
                userService.activateUser(id);

                return new ApiResponse<>(
                                200,
                                "User activated successfully",
                                null);
        }

        @GetMapping("/organization/{organizationId}")
        public ApiResponse<List<UserResponse>> getUsersByOrganization(
                        @PathVariable Long organizationId) {
                List<User> users = userService.getUsersByOrganizationId(organizationId);

                List<UserResponse> responses = users.stream()
                                .map(userMapper::toResponse)
                                .toList();

                return new ApiResponse<>(
                                200,
                                "Users retrieved successfully",
                                responses);
        }
}