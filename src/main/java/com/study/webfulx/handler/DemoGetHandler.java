package com.study.webfulx.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Component
public class DemoGetHandler {

    /**
     * 인수 없이 문자열을 반환하는 경우
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> demoGet1(ServerRequest req) {
        return ServerResponse.ok().body(Mono.just("Hello"), String.class);
    }

    /**
     * URL에 인수가 있는 경우
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> demoGet2(ServerRequest req) {
        String name = req.pathVariable("name");
        return ServerResponse.ok().body(Mono.just("Hello " + name), String.class);
    }

    /**
     * 쿼리 스트링에 인수가 있는 경우
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> demoGet3(ServerRequest req) {
        Optional<String> name = req.queryParam("name");
        return ServerResponse.ok().body(Mono.just("Hello ! " + name.orElse("Anonymous")), String.class);
    }

    /**
     * 모든 쿼리 스트링을 취득하는 경우
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> demoGet4(ServerRequest req) {
        return ServerResponse.ok().body(Mono.just(req.queryParams()), Map.class);
    }

    @Bean
    public RouterFunction<ServerResponse> getRouteRule() {
        return RouterFunctions.route(RequestPredicates.GET("/demo/get/1"), this::demoGet1)
                .andRoute(RequestPredicates.GET("/demo/get/2/{name}"), this::demoGet2)
                .andRoute(RequestPredicates.GET("/demo/get/3"), this::demoGet3)
                .andRoute(RequestPredicates.GET("/demo/get/4"), this::demoGet4);
    }
}
