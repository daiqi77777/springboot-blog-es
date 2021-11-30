package com.imooc.common;

import com.imooc.entity.mysql.MysqlCompetitorInfo;
import com.imooc.repository.MysqlCompetitorInfoRepository;
import com.imooc.utils.AnalyzerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class StaticScheduleTask {
    //每隔5秒执行一次：*/5 * * * * ?
    //每隔1分钟执行一次：0 */1 * * * ?
    //每天23点执行一次：0 0 23 * * ?
    //每天凌晨1点执行一次：0 0 1 * * ?
    //每月1号凌晨1点执行一次：0 0 1 1 * ?
    //每月最后一天23点执行一次：0 0 23 L * ?
    //每周星期六凌晨1点实行一次：0 0 1 ? * L
    //在26分、29分、33分执行一次：0 26,29,33 * * * ?
    //每天的0点、13点、18点、21点都执行一次：0 0 0,13,18,21 * * ?
    @Autowired
    MysqlCompetitorInfoRepository mysqlCompetitorInfoRepository;

    //3.添加定时任务
    // @Scheduled(cron = "0 */1 * * * ?")
     @Scheduled(cron = "0 0 17 * * ?")
    private void configureTasks() throws IOException {
        log.info("执行静态定时任务开始时间: " + LocalDateTime.now());
        List<MysqlCompetitorInfo> MysqlCompetitorInfos = mysqlCompetitorInfoRepository.queryEmptyCompetitorTitleSegment();
        for (MysqlCompetitorInfo t : MysqlCompetitorInfos) {
            List<String> str_arr = AnalyzerUtil.getAnalyzer(t.getCompetitorTitle());
            if (str_arr.size() == 0) continue;
            t.setCompetitorTitleSegment(StringUtils.join(str_arr, ','));
            mysqlCompetitorInfoRepository.save(t);
        }
        log.info("执行静态定时任务结束时间: " + LocalDateTime.now());
    }
}