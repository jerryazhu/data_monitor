package com.qa.data.visualization.repositories;

import com.qa.data.visualization.entities.ClassToolsDailyActivity;
import com.qa.data.visualization.entities.LastError;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LastErrorRepository extends CrudRepository<LastError, Integer> {
}
