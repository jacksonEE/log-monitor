package com.jackson.log.mongo;

import com.jackson.log.mongo.support.AbstractMongoConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@ConfigurationProperties(prefix = "mongodb.db1")
@EnableMongoRepositories(basePackages = {"com.jackson.log.repository.db1"},
        mongoTemplateRef = "dbOneMongoTemplate")
public class DbOneConfig extends AbstractMongoConfig {

    @Bean(name = "dbOneMongoTemplate")
    @Override
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }

}
