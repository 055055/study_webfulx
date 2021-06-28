package com.study.webfulx.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class RouteHandler {

    private final DemoGetHandler demoGetHandler;
    private final DemoPostHandler demoPostHandler;
    private final FluxDemoHandler fluxDemoHandler;
    private final ClientDemoHandler clientDemoHandler;

    @Bean
    public RouterFunction<ServerResponse> route(){
        return this.demoGetHandler.getRouteRule()
                .and(demoPostHandler.postRouteRule())
                .and(fluxDemoHandler.fluxRouterRule())
                .and(clientDemoHandler.clientRouterRule());
    }
}
