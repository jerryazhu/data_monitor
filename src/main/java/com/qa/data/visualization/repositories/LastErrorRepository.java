package com.qa.data.visualization.repositories;

import com.qa.data.visualization.entities.LastError;
import org.springframework.data.repository.CrudRepository;

public interface LastErrorRepository extends CrudRepository<LastError, Long> {
}
