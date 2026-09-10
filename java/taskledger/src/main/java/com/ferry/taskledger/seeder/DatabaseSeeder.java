package com.ferry.taskledger.seeder;

import com.ferry.taskledger.entity.Organization;
import com.ferry.taskledger.entity.OrganizationStatus;
import com.ferry.taskledger.repository.OrganizationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final OrganizationRepository organizationRepository;

    public DatabaseSeeder(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public void run(String... args) {
        if (organizationRepository.count() == 0) {
            Organization organization = new Organization();

            organization.setName("TaskLedger Organization");
            organization.setDescription("Default organization for TaskLedger");
            organization.setStatus(OrganizationStatus.ACTIVE);

            organizationRepository.save(organization);
        }
    }
}