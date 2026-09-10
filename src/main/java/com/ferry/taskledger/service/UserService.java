package com.ferry.taskledger.service;

import com.ferry.taskledger.dto.request.CreateUserRequest;
import com.ferry.taskledger.dto.request.UpdateUserRequest;
import com.ferry.taskledger.entity.Organization;
import com.ferry.taskledger.entity.OrganizationStatus;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserStatus;
import com.ferry.taskledger.repository.OrganizationRepository;
import com.ferry.taskledger.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public UserService(
            UserRepository userRepository,
            OrganizationRepository organizationRepository
    ) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Organization organization = organizationRepository.findById(
                request.getOrganizationId()
        ).orElseThrow(() ->
                new NoSuchElementException("Organization not found")
        );

        if (organization.getStatus() == OrganizationStatus.INACTIVE) {
            throw new IllegalArgumentException("Organization is inactive");
        }

        User user = new User();

        user.setOrganization(organization);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        // Status dikelola backend
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    public User updateUser(
        Long id,
        UpdateUserRequest request
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> 
                    new NoSuchElementException("User not found")
                );
        
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("User is inactive");
        }

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        return userRepository.save(user);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found")
                );

        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
    }

    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("User not found")
                );

        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }

    public List<User> getUsersByOrganizationId(Long organizationId) {

        if (!organizationRepository.existsById(organizationId)) {
            throw new NoSuchElementException("Organization not found");
        }

        return userRepository.findByOrganizationId(organizationId);
    }
}