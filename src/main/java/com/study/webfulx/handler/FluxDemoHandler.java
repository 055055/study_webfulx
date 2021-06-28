package com.study.webfulx.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

@Component
public class FluxDemoHandler {

    /**
     * 0부터 24를 순차적으로 텍스트 스트림으로 출력하는 경우
     * @param req
     * @return
     */
    public Mono<ServerResponse> fluxHandler(ServerRequest req) {
        Flux<Integer> stream = Flux.fromStream(IntStream.range(0, 24).boxed());
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
                .body(stream, Integer.class);
    }

    public RouterFunction<ServerResponse> fluxRouterRule() {
        return RouterFunctions.route(RequestPredicates.GET("/flux/1"), this::fluxHandler);
    }
}
