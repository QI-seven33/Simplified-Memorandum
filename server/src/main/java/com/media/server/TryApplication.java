package com.media.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.web.bind.annotation.CrossOrigin;


@Slf4j
//@EnableCaching//开启缓存数据注解功能
@EnableScheduling//开启任务调度
@SpringBootApplication(scanBasePackages = "com.media")
@MapperScan("com.media.server.mapper")
public class TryApplication {
    public static void main(String[] args) {
        SpringApplication.run(TryApplication.class, args);
        log.info("server started");
    }
}
