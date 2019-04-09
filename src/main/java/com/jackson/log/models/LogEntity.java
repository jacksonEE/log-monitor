package com.jackson.log.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * created by Jackson at 2018/12/2 10:21
 **/
@Document(collection = "log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntity {

    @Id
    private String id;

    private String thread;

    @Indexed
    private String level;

    private Integer pid;

    private String msg;

    @Indexed
    private String env;

    private String time;

    @Indexed
    private String service;

    private String clazz;

    @Indexed
    private String traceId;
}
