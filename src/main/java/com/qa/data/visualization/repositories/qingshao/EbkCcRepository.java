package com.qa.data.visualization.repositories.qingshao;

import com.qa.data.visualization.entities.qingshao.EbkCC;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by dykj on 2016/12/28.
 */
public interface EbkCcRepository extends CrudRepository<EbkCC, Long> {
    @Query(value = "select s from EbkCC s where s.id like CONCAT(:id, '%') and s.department=4")
    public List<EbkCC> getEbkCcById(@Param("id") String id, Pageable pageable);

    @Query(value = "select s from EbkCC s where s.nickname like CONCAT(:nickname, '%') and s.department=4")
    public List<EbkCC> getEbkCcByName(@Param("nickname") String nickname, Pageable pageable);
}
