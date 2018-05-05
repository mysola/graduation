package com.wangyang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@MapperScan({"com.wangyang.mapper","com.wangyang.docIndex"})
public class GraduationApp {
    public static void main(String[] args) {
        SpringApplication.run(GraduationApp.class, args);
    }

}
