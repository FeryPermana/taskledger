package com.ferry.taskledger.repository;

import com.ferry.taskledger.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}