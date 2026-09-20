package com.ferry.taskledger.dto.response;

public class LoginResponse {

    private Long userId;
    private Long organizationId;
    private String name;
    private String email;
    private String role;
    private String token;

    public LoginResponse(
            Long userId,
            Long organizationId,
            String name,
            String email,
            String role,
            String token
    ) {
        this.userId = userId;
        this.organizationId = organizationId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}