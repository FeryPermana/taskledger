package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.request.LoginRequest;
import com.ferry.taskledger.dto.response.LoginResponse;
import com.ferry.taskledger.response.ApiResponse;
import com.ferry.taskledger.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);

        return new ApiResponse<>(
                200,
                "Login successful",
                response
        );
    }
}