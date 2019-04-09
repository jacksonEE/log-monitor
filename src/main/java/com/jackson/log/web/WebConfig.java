package com.jackson.log.web;

import com.jackson.log.handlers.LogHandler;
import com.jackson.log.handlers.ServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * created by Jackson at 2018/12/2 11:25
 **/
@Configuration
public class WebConfig {

    @Autowired
    private LogHandler logHandler;
    @Autowired
    private ServiceHandler serviceHandler;

    @Bean
    public RouterFunction<ServerResponse> route(
            @Value("classpath:/templates/index.html") final Resource indexHtml) {
        return RouterFunctions
                .route(GET("/index"), request -> ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml))
                .andRoute(GET("/log/list"), request -> logHandler.listLogs(request))
                .andRoute(GET("/service/list"), request -> serviceHandler.listService())
                .andRoute(POST("/service/create/{name}"), request -> serviceHandler.create(request))
                .andRoute(POST("/service/delete/{id}"), request -> serviceHandler.delete(request));
    }
}
