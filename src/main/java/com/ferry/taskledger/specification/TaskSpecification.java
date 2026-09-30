package com.ferry.taskledger.specification;

import com.ferry.taskledger.entity.Task;
import com.ferry.taskledger.entity.TaskPriority;
import com.ferry.taskledger.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class TaskSpecification {

    public static Specification<Task> titleContains(String search) {
        return (root, query, cb) -> {

            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("title")),
                    "%" + search.trim().toLowerCase() + "%"
            );
        };
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> {

            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, cb) -> {

            if (priority == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Task> hasProjectId(Long projectId) {
        return (root, query, cb) -> {

            if (projectId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("project").get("id"),
                    projectId
            );
        };
    }

    public static Specification<Task> hasAssigneeId(Long assigneeId) {
        return (root, query, cb) -> {

            if (assigneeId == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("assignee").get("id"),
                    assigneeId
            );
        };
    }

    public static Specification<Task> projectIdIn(
            List<Long> projectIds) {

        return (root, query, cb) -> {

            if (projectIds == null || projectIds.isEmpty()) {
                return cb.disjunction();
            }

            return root.get("project")
                    .get("id")
                    .in(projectIds);
        };
    }
}