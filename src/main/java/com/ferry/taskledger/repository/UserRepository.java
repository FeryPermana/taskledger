package com.ferry.taskledger.repository;

import com.ferry.taskledger.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<User> findByOrganizationId(Long organizationId);

    Optional<User> findByEmail(String email);
}