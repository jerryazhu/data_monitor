package com.qa.data.visualization.auth.repositories;

import com.qa.data.visualization.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}