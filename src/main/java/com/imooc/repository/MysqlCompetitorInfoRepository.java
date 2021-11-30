package com.imooc.repository;

import com.imooc.entity.mysql.MysqlCompetitorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * @author sixkery
 * @date 2019/11/24
 */
public interface MysqlCompetitorInfoRepository extends JpaRepository<MysqlCompetitorInfo, Integer> {

    @Query("select e from MysqlCompetitorInfo e where e.id<100 order by e.createdAt desc ")
    List<MysqlCompetitorInfo> queryAll();

    @Query("select e from MysqlCompetitorInfo e  order by id desc ")
    List<MysqlCompetitorInfo> queryEmptyCompetitorTitleSegment();

    MysqlCompetitorInfo findByProductAuditId(int keyword);

}
