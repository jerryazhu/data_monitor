package com.qa.data.visualization.auth.repositories;

import com.qa.data.visualization.auth.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
