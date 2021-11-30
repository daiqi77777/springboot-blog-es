package com.imooc.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.util.Date;

/**
 * @author dq
 * @date 2021-11-23
 */
@Data
/**
 * useServerConfiguration = true 使用线上的配置,createIndex 在项目启动的时候不要创建索引，通常在 kibana 中已经配置过了
 */

@Document(indexName = "competitor_info", type = "_doc", useServerConfiguration = true, createIndex = false)
public class EsCompetitorInfo {

    @Id
    private Integer id;

    @Field(type = FieldType.Integer)
    private Integer product_audit_id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String competitor_asin;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String competitor_title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String competitor_title_segment;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    private Date created_at;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    private Date updated_at;

}
