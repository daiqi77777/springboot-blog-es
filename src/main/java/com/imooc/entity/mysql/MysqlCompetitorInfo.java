package com.imooc.entity.mysql;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;


@Data
@Table(name = "competitor_info")
@Entity
public class MysqlCompetitorInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer productAuditId;
    private String competitorAsin;
    private String competitorTitle;
    private String competitorTitleSegment;
    private Date createdAt;
    private Date updatedAt;


}
