package com.imooc.controller;

import com.imooc.repository.MysqlCompetitorInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    MysqlCompetitorInfoRepository mysqlCompetitorInfoRepository;

    @GetMapping("/")
    public String index() {

        return "index.html";

    }
}
