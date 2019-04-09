package com.jackson.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class LogApplication implements ApplicationListener<ApplicationReadyEvent> {

    public static void main(String[] args) {
        new SpringApplicationBuilder(LogApplication.class).run(args);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("=========================================");
        log.info("*******LogApplication**启动成功********");
        log.info("=========================================");
    }
}
