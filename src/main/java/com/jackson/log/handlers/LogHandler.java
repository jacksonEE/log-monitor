package com.jackson.log.handlers;

import com.jackson.log.models.LogEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * created by Jackson at 2018/12/3 10:21
 **/
@Service
public class LogHandler {

    @Autowired
    @Qualifier("dbOneMongoTemplate")
    private MongoTemplate dbOneMongoTemplate;

    @Autowired
    @Qualifier("dbTwoMongoTemplate")
    private MongoTemplate dbTwoMongoTemplate;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");

    public Mono<ServerResponse> listLogs(ServerRequest request) {
        Query query = new Query();
        int index = Integer.parseInt(request.queryParam("index").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String env = request.queryParam("env").orElse(null);
        String service = request.queryParam("service").orElse(null);
        String message = request.queryParam("message").orElse(null);
        String level = request.queryParam("level").orElse(null);
        String startTime = request.queryParam("startTime").orElse(null);
        String endTime = request.queryParam("endTime").orElse(null);
        String searchType = request.queryParam("searchType").orElse("msg");
        String project = request.queryParam("project").orElse("jackson");

        query.with(PageRequest.of(index > 0 ? index - 1 : index, size));
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(env)) {
            criteria.and("env").is(env);
        }
        if (!StringUtils.isEmpty(service)) {
            criteria.and("service").is(service);
        }
        if (!StringUtils.isEmpty(message)) {
            if (searchType.equals("msg")) {
                criteria.and("msg").regex(".*" + message + ".*", "i");
            } else {
                criteria.and("traceId").is(message);
            }

        }
        if (!StringUtils.isEmpty(level)) {
            criteria.and("level").is(level);
        }

        query.addCriteria(criteria);

        if (!StringUtils.isEmpty(startTime)) {
            Date date = new Date(Long.parseLong(startTime));
            query.addCriteria(Criteria.where("time").gte(SDF.format(date)));
        }

        if (!StringUtils.isEmpty(endTime)) {
            Date date = new Date(Long.parseLong(endTime));
            query.addCriteria(Criteria.where("time").lt(SDF.format(date)));
        }

        query.with(Sort.by(Sort.Direction.DESC, "time"));

        /*MongoTemplate targetTemplate = project.equals("jackson") ? dbOneMongoTemplate : dbTwoMongoTemplate;
        List<LogEntity> logEntities = targetTemplate.find(query, LogEntity.class);
        long count = targetTemplate.count(query, LogEntity.class);
        Page page = new Page(logEntities, count);*/
        ArrayList<LogEntity> LogEntities = new ArrayList<>() {{
            add(new LogEntity("wrewr54gfd5dd56e", "ve.com.cn:11072","INFO",
                    1,"Analyzing class class com.jackson.log.models.ServiceEntity for index information.",
                    "prd",
                    "2019-01-09 21:55:21.082",
                    "gateway",
                    "com.jackson.log.LogApplication",
                    "8592"));
            add(new LogEntity("wrewr54gfd5dd56e", "","DEBUG",
                    1,"Discovered cluster type of STANDALONE",
                    "test",
                    "2019-01-09 21:55:21.082",
                    "gateway",
                    "com.jackson.log.LogApplication",
                    "8592"));
            add(new LogEntity("wrewr54gfd5dd56e", "ve.com.cn:11072","ERROR",
                    1,"Bootstrapping Spring Data repositories in DEFAULT mode.",
                    "dev",
                    "2019-02-09 21:55:21.082",
                    "gateway",
                    "com.jackson.log.LogApplication",
                    "8592"));
        }};
        Page page = new Page(LogEntities, 1);
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(page));
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Page {
        private List<LogEntity> data;
        private long count;
    }
}
