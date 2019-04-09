package com.jackson.log.mongo.support;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import lombok.Data;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.util.Collections;


@Data
public abstract class AbstractMongoConfig {

    protected String host;
    protected int port;
    protected String database;

    protected String username;
    protected String password;

    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(mongoClient(), database);
    }

    protected MongoClient mongoClient() {
        return new MongoClient(
                Collections.singletonList(new ServerAddress(host, port)),
                MongoCredential.createCredential(username, database, password.toCharArray()),
                MongoClientOptions.builder().connectTimeout(5000).build()
        );
    }

    public abstract MongoTemplate mongoTemplate();
}











