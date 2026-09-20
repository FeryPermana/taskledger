package com.ferry.taskledger.repository;

import com.ferry.taskledger.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    List<Task> findByProjectIdIn(List<Long> projectIds);
}