package com.qa.data.visualization.auth.repositories;

import com.qa.data.visualization.auth.entities.Role;
import com.qa.data.visualization.auth.entities.Role2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Role2Repository extends JpaRepository<Role2, Long> {
    Role findByName(String name);
}
