package com.qa.data.visualization.repositories;

import com.qa.data.visualization.entities.LastDebug;
import com.qa.data.visualization.entities.LastError;
import org.springframework.data.repository.CrudRepository;

public interface LastDebugRepository extends CrudRepository<LastDebug, Integer> {
}
