package com.imooc.controller;

import com.imooc.entity.mysql.MysqlCompetitorInfo;
import com.imooc.repository.EsCompetitorInfoRepository;
import com.imooc.repository.MysqlCompetitorInfoRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sixkery
 * @date 2019/11/24
 */
@RestController
@Slf4j
public class DataController {

    @Autowired
    MysqlCompetitorInfoRepository mysqlCompetitorInfoRepository;

    @Autowired
    EsCompetitorInfoRepository esCompetitorInfoRepository;


    @GetMapping("/competitorInfo")
    public Object competitorInfo() {
        return mysqlCompetitorInfoRepository.queryAll();
    }


    @GetMapping("/competitorInfoSearch")
    public Object competitorInfoSearch(Integer productAuditId,String keyword,Integer similarity,Integer page,Integer size) {
        Map<String, Object> map = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        // 统计耗时
        StopWatch watch = new StopWatch();
        watch.start();
        MysqlCompetitorInfo asinTitle = mysqlCompetitorInfoRepository.findByProductAuditId(productAuditId);
        if (asinTitle == null) {
            map.put("code",400);
            map.put("msg","审核产品ID：" +productAuditId + " 未找到");
            return map;
        }

        String[] word_segment = asinTitle.getCompetitorTitleSegment().split(",");
        //主查询对象
        BoolQueryBuilder builder = QueryBuilders.boolQuery();


        //should查询对象
        BoolQueryBuilder wordSegmentShouldBuilder = QueryBuilders.boolQuery();
        for (String s : word_segment) {
            wordSegmentShouldBuilder.should(QueryBuilders.matchPhraseQuery("competitor_title_segment", s));
        }
        // 3<90%
        //表示如果可选子句的数量等于（或小于）设置的值，则它们都是必需的，但如果它大于设置的值，则适用规范。
        // 在这个例子中：如果有1到3个子句，则它们都是必需的，但是对于4个或更多子句，只需要90％的匹配度
       int  a = word_segment.length * similarity/100 ;
        wordSegmentShouldBuilder.minimumShouldMatch(a+"<"+similarity.toString() + "%");

        //将should查询对象设置到 must 方法中
        builder.must(wordSegmentShouldBuilder);

        //不找自己
        builder.mustNot(QueryBuilders.matchQuery("product_audit_id", productAuditId));

        //找标题中必须包含这些词的
        if (!keyword.equals("")) {
            String[] keyword_arr = keyword.split(",");
            BoolQueryBuilder keywordShouldBuilder = QueryBuilders.boolQuery();
            for (String k : keyword_arr) {
                keywordShouldBuilder.should(QueryBuilders.matchPhraseQuery("competitor_title_segment", k));
            }
            keywordShouldBuilder.minimumShouldMatch("100%");
            builder.must(keywordShouldBuilder);
        }
        log.info("s={}", builder.toString());

       /* Page<EsAsinTitle> search = (Page<EsAsinTitle>) asinTitleBlogRepository.search(builder);
         List<EsAsinTitle> content = search.getContent(); */
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .withSort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(pageable)
                .build();

        AggregatedPage search = (AggregatedPage) esCompetitorInfoRepository.search(query);
        List content = search.getContent();
        long totalElements = search.getTotalElements();
        int totalPages = search.getTotalPages();
        watch.stop();
        // 计算耗时
        long millis = watch.getTotalTimeMillis();
        map.put("code",200);
        map.put("duration", millis);
        map.put("total", totalElements);
        map.put("totalPages", totalPages);
        map.put("list", content);
        return map;
    }

    /* 可参考匹配方法
    {"query":{
      "match":{
         "字段名":{
            "query":"查询内容",
            "operator":"or",
            "minimum_should_match":"70%"
         }
      }
   }
}
*/
}
