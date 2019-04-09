package com.jackson.log.repository.db1;

import com.jackson.log.models.ServiceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * created by Jackson at 2018/12/3 20:23
 **/
public interface ServiceRepository extends MongoRepository<ServiceEntity, String> {
}
