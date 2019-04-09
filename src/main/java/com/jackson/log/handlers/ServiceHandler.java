package com.jackson.log.handlers;

import com.jackson.log.models.ServiceEntity;
import com.jackson.log.repository.db1.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * created by Jackson at 2018/12/3 20:24
 **/
@Service
public class ServiceHandler {

    @Autowired
    private ServiceRepository serviceRepository;

    public Mono<ServerResponse> listService() {
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(Flux.fromIterable(serviceRepository.findAll()),
                ServiceEntity.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        String name = request.pathVariable("name");
        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setName(name);
        ServiceEntity insert = serviceRepository.insert(serviceEntity);
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(insert));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        serviceRepository.deleteById(id);
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(Mono.just(id)));
    }
}
