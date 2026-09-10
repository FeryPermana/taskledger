package com.ferry.taskledger.repository;

import com.ferry.taskledger.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}