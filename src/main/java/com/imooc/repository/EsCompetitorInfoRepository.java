package com.imooc.repository;

import com.imooc.entity.es.EsCompetitorInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface EsCompetitorInfoRepository extends ElasticsearchRepository<EsCompetitorInfo, Integer> {
}
