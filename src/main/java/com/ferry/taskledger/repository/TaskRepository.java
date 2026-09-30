package com.ferry.taskledger.repository;

import com.ferry.taskledger.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository
        extends JpaRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {

    List<Task> findByProjectId(Long projectId);

    Page<Task> findByProjectId(
            Long projectId,
            Pageable pageable);

    List<Task> findByProjectIdIn(
            List<Long> projectIds);

    Page<Task> findByProjectIdIn(
            List<Long> projectIds,
            Pageable pageable);
}