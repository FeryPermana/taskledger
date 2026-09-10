package com.ferry.taskledger.repository;

import com.ferry.taskledger.entity.ProjectMember;
import com.ferry.taskledger.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    @Query("""
            SELECT pm
            FROM ProjectMember pm
            JOIN pm.user u
            WHERE pm.project.id = :projectId
              AND u.status = :status
            """)
    List<ProjectMember> findMembersByProjectAndUserStatus(
            @Param("projectId") Long projectId,
            @Param("status") UserStatus status
    );

    void deleteByProjectIdAndUserId(Long projectId, Long userId);
}