package com.ferry.taskledger.service;

import com.ferry.taskledger.entity.ActivityLog;
import com.ferry.taskledger.entity.Organization;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserRole;
import com.ferry.taskledger.repository.ActivityLogRepository;
import com.ferry.taskledger.repository.OrganizationRepository;
import com.ferry.taskledger.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ActivityLogService {

        private final ActivityLogRepository activityLogRepository;
        private final OrganizationRepository organizationRepository;
        private final UserRepository userRepository;

        public ActivityLogService(
                        ActivityLogRepository activityLogRepository,
                        OrganizationRepository organizationRepository,
                        UserRepository userRepository) {
                this.activityLogRepository = activityLogRepository;
                this.organizationRepository = organizationRepository;
                this.userRepository = userRepository;
        }

        public ActivityLog createActivityLog(
                        Long organizationId,
                        Long userId,
                        String action,
                        String entityType,
                        Long entityId,
                        String description) {

                Organization organization = organizationRepository.findById(organizationId)
                                .orElseThrow(() -> new NoSuchElementException("Organization not found"));

                User user = null;

                if (userId != null) {

                        user = userRepository.findById(userId)
                                        .orElseThrow(() -> new NoSuchElementException("User not found"));

                        if (!user.getOrganization().getId()
                                        .equals(organization.getId())) {

                                throw new IllegalArgumentException(
                                                "User does not belong to organization");
                        }
                }

                ActivityLog activityLog = new ActivityLog();

                activityLog.setOrganization(organization);
                activityLog.setUser(user);
                activityLog.setAction(action);
                activityLog.setEntityType(entityType);
                activityLog.setEntityId(entityId);
                activityLog.setDescription(description);

                return activityLogRepository.save(activityLog);
        }

        public List<ActivityLog> getByEntity(
                        String entityType,
                        Long entityId) {

                User authenticatedUser = getAuthenticatedUser();

                List<ActivityLog> logs = activityLogRepository
                                .findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
                                                entityType,
                                                entityId);

                if (logs.isEmpty()) {
                        return List.of();
                }

                Long organizationId = logs.get(0)
                                .getOrganization()
                                .getId();

                validateOrganizationAccess(
                                organizationId,
                                authenticatedUser);

                return logs;
        }

        public List<ActivityLog> getByOrganization(
                        Long organizationId) {

                if (!organizationRepository.existsById(organizationId)) {
                        throw new NoSuchElementException(
                                        "Organization not found");
                }

                User authenticatedUser = getAuthenticatedUser();

                validateOrganizationAccess(
                                organizationId,
                                authenticatedUser);

                return activityLogRepository
                                .findByOrganizationIdOrderByCreatedAtDesc(
                                                organizationId);
        }

        public List<ActivityLog> getByUser(
                        Long userId) {

                User targetUser = userRepository.findById(userId)
                                .orElseThrow(() -> new NoSuchElementException("User not found"));

                User authenticatedUser = getAuthenticatedUser();

                validateOrganizationAccess(
                                targetUser.getOrganization().getId(),
                                authenticatedUser);

                return activityLogRepository
                                .findByUserIdOrderByCreatedAtDesc(
                                                userId);
        }

        private User getAuthenticatedUser() {

                Authentication authentication = SecurityContextHolder.getContext()
                                .getAuthentication();

                String email = authentication.getName();

                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new NoSuchElementException("User not found"));
        }

        private void validateOrganizationAccess(
                        Long organizationId,
                        User authenticatedUser) {

                if (authenticatedUser.getRole() == UserRole.SUPER_ADMIN) {
                        return;
                }

                if (!authenticatedUser.getOrganization()
                                .getId()
                                .equals(organizationId)) {

                        throw new AccessDeniedException(
                                        "You do not have access to these activity logs");
                }
        }
}