package com.ferry.taskledger.service;

import com.ferry.taskledger.entity.OrganizationStatus;
import com.ferry.taskledger.dto.request.CreateOrganizationRequest;
import com.ferry.taskledger.dto.request.UpdateOrganizationRequest;
import com.ferry.taskledger.entity.Organization;
import com.ferry.taskledger.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization createOrganization(CreateOrganizationRequest request) {

        Organization organization = new Organization();

        organization.setName(request.getName());
        organization.setDescription(request.getDescription());
        organization.setStatus(OrganizationStatus.ACTIVE);

        return organizationRepository.save(organization);
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Organization not found"));
    }

    public Organization updateOrganization(
        Long id,
        UpdateOrganizationRequest request
    ) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Organization not found")
                );

        organization.setName(request.getName());
        organization.setDescription(request.getDescription());

        return organizationRepository.save(organization);
    }

    public void deleteOrganization(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Organization not found")
                );

        organizationRepository.delete(organization);
    }
}