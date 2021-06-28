package com.study.webfulx.handler;

import com.study.webfulx.utils.ConvertUtil;
import com.study.webfulx.utils.Output;
import com.study.webfulx.utils.SampleDto;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
public class DemoPostHandler {

    /**
     * 송신된 데이터를 문자열로 취급해 그대로 반환하는 경우
     * @param req
     * @return
     */
    public Mono<ServerResponse> demoPost1(ServerRequest req) {
        return ServerResponse.ok().body(req.bodyToMono(String.class), String.class);
    }

    /**
     * x-www-form-urlencoded에 의해 폼이 수신된 경우
     * @param req
     * @return
     */
    public Mono<ServerResponse> demoPost2(ServerRequest req) {
        Mono<MultiValueMap<String, String>> form = req.formData();
        return ServerResponse.ok().body(form, new ParameterizedTypeReference<MultiValueMap<String, String>>() {
        });
    }

    /**
     * Body에 JSON이 송신된 경우
     * @param req
     * @return
     */
    public Mono<ServerResponse> demoPost3(ServerRequest req) {
        return ServerResponse.ok().body(req.bodyToMono(SampleDto.class).map(ConvertUtil::convert), Output.class);
    }

    public RouterFunction<ServerResponse> postRouteRule() {
        return RouterFunctions.route(RequestPredicates.POST("/demo/post/1"), this::demoPost1)
                .andRoute(RequestPredicates.POST("/demo/post/2"), this::demoPost2)
                .andRoute(RequestPredicates.POST("/demo/post/3"), this::demoPost3);
    }
}
