package com.jackson.log.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * created by Jackson at 2018/12/3 20:21
 **/
@Document
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {

    @Id
    private String id;

    private String name;
}
