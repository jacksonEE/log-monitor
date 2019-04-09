package com.jackson.log.mongo;

import com.jackson.log.mongo.support.AbstractMongoConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@ConfigurationProperties(prefix = "mongodb.db2")
@EnableMongoRepositories(basePackages = {"com.jackson.log.repository.yoyo"},
        mongoTemplateRef = "dbTwoMongoTemplate")
public class YoyoDbConfig extends AbstractMongoConfig {

    @Bean(name = "dbTwoMongoTemplate")
    @Override
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }

}
