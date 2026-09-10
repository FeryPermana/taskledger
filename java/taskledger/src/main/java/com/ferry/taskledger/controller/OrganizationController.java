package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.response.OrganizationResponse;
import com.ferry.taskledger.entity.Organization;
import com.ferry.taskledger.mapper.OrganizationMapper;
import com.ferry.taskledger.response.ApiResponse;
import com.ferry.taskledger.service.OrganizationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ferry.taskledger.dto.request.CreateOrganizationRequest;
import com.ferry.taskledger.dto.request.UpdateOrganizationRequest;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;

    public OrganizationController(
            OrganizationService organizationService,
            OrganizationMapper organizationMapper
    ) {
        this.organizationService = organizationService;
        this.organizationMapper = organizationMapper;
    }

    @GetMapping
    public ApiResponse<List<OrganizationResponse>> getAllOrganizations() {

        List<Organization> organizations =
                organizationService.getAllOrganizations();

        List<OrganizationResponse> responses = organizations.stream()
                .map(organizationMapper::toResponse)
                .toList();

        return new ApiResponse<>(
                200,
                "Organizations retrieved successfully",
                responses
        );
    }

    @PostMapping
    public ApiResponse<OrganizationResponse> createOrganization(
       @Valid @RequestBody CreateOrganizationRequest request
    ) {
        Organization organization = organizationService.createOrganization(request);   

        OrganizationResponse response = organizationMapper.toResponse(organization);

        return new ApiResponse<>(
                201,
                "Organization created successfully",
                response
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<OrganizationResponse> getOrganizationById(
        @PathVariable Long id
    ) {
        Organization organization = organizationService.getOrganizationById(id);

        OrganizationResponse response = organizationMapper.toResponse(organization);

        return new ApiResponse<>(
                200,
                "Organization retrieved successfully",
                response
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<OrganizationResponse> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrganizationRequest request
    ) {
        Organization organization =
                organizationService.updateOrganization(id, request);

        OrganizationResponse response =
                organizationMapper.toResponse(organization);

        return new ApiResponse<>(
                200,
                "Organization updated successfully",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrganization(
            @PathVariable Long id
    ) {
        organizationService.deleteOrganization(id);

        return new ApiResponse<>(
                200,
                "Organization deleted successfully",
                null
        );
    }
}