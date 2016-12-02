package com.qa.data.visualization.repositories.qingshao;

import com.qa.data.visualization.entities.qingshao.EbkTeacher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EbkTeachersRepository extends CrudRepository<EbkTeacher, Long> {
    @Query(value = "select s from EbkTeacher s where s.id like CONCAT(:id, '%')")
    public List<EbkTeacher> getEbkTeachersById(@Param("id") String id, Pageable pageable);

    @Query(value = "select s from EbkTeacher s where s.email like CONCAT(:email, '%')")
    public List<EbkTeacher> getEbkTeachersByEmail(@Param("email") String email, Pageable pageable);
}