package com.study.webfulx.handler;

import com.study.webfulx.utils.GitHubDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Configuration
public class ClientDemoHandler {

    public static final String GITHUB_BASE_URL = "https://api.github.com";

    /**
     * WebClinet를 사용한 경우(GET)
     * @param req
     * @return
     */
    public Mono<ServerResponse> clientDemo1(ServerRequest req) {
        WebClient webClient = WebClient.create(GITHUB_BASE_URL);

        //쿼리 스트링에 user가 포함되어 있는지 체크
        Optional<String> userName = req.queryParam("user");
        if (!userName.isPresent()) {
            return ServerResponse.ok().body(Mono.just("user is Required"), String.class);
        }

        //비동기 호출(비블록)
        Mono<List<GitHubDto>> values = webClient.get()
                .uri("users/" + userName.get() + "/repos")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GitHubDto>>() {
                });


        values.subscribe(System.out::println);


        return ServerResponse.ok().body(values, new ParameterizedTypeReference<List<GitHubDto>>() {
        });
    }

    @Bean
    public RouterFunction<ServerResponse> clientRouterRule() {
        return RouterFunctions.route(RequestPredicates.GET("/client/get/1"), this::clientDemo1);
    }
}
