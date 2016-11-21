package com.qa.data.visualization.repositories.qingshao;

import com.qa.data.visualization.entities.qingshao.EbkStudent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EbkStudentsRepository extends CrudRepository<EbkStudent, Long> {
    @Query(value = "select s from EbkStudent s where s.id like CONCAT(:id, '%')")
    public List<EbkStudent> getEbkStudentsById(@Param("id") String id, Pageable pageable);

    @Query(value = "select s from EbkStudent s where s.mobile like CONCAT(:mobile, '%')")
    public List<EbkStudent> getEbkStudentsByMobile(@Param("mobile") String mobile, Pageable pageable);
}
