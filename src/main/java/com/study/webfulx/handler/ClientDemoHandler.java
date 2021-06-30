package com.study.webfulx.handler;

import com.study.webfulx.utils.GitHubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ClientDemoHandler {

    private final WebClient webClient;
    public static final String GITHUB_BASE_URL = "https://api.github.com";

    /**
     * WebClinet를 사용한 경우(GET)
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> clientDemo1(ServerRequest req) {
        //쿼리 스트링에 user가 포함되어 있는지 체크
        Optional<String> userName = req.queryParam("user");
        if (!userName.isPresent()) {
            return ServerResponse.ok().body(Mono.just("user is Required"), String.class);
        }

        //비동기 호출(비블록)
        Mono<List<GitHubDto>> values = webClient.get()
                .uri(GITHUB_BASE_URL + "/users/{name}/repos", userName.get())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

        values.subscribe(System.out::println);

        return ServerResponse.ok().body(values, new ParameterizedTypeReference<>() {
        });
    }

    /**
     * simple retry
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> clientDemo2(ServerRequest req) {
        //비동기 호출(비블록)
        Mono<List<GitHubDto>> values = webClient.get()
                .uri("http://www.naver.com")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GitHubDto>>() {
                }).retry(3);

        values.subscribe(System.out::println);

        return ServerResponse.ok().body(values, new ParameterizedTypeReference<>() {
        });
    }

    /**
     * retryWhen 2초 간격으로 3번 재시도
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> clientDemo3(ServerRequest req) {
        //비동기 호출(비블록)
        Mono<List<GitHubDto>> values = webClient.get()
                .uri("http://www.naver.com")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GitHubDto>>() {
                }).retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)));

        values.subscribe(System.out::println);

        return ServerResponse.ok().body(values, new ParameterizedTypeReference<>() {
        });
    }

    /**
     * retryWhen 점진적으로 초를 증가해서 총 3번 시도 (1회 2초 증가, 2회 4초 증가 3회 8초)
     * backoff 재시도 횟수가 증가할수록 backoff 시간 증가로 네트워크에 갑작스런 트래픽 부하 회피
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> clientDemo4(ServerRequest req) {
        //비동기 호출(비블록)
        Mono<List<GitHubDto>> values = webClient.get()
                .uri("http://www.naver.com")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GitHubDto>>() {
                }).retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));

        values.subscribe(System.out::println);

        return ServerResponse.ok().body(values, new ParameterizedTypeReference<>() {
        });
    }

    /**
     * retryWhen시 backoff, jitter사용
     * jitter(지연변이) 동일한 재시도 시간 간격에 무작위성 추가하여 동시성 회피 즉 backoff + 랜덤 시간으로 동일 재시도시 분산
     *
     * @param req
     * @return
     */
    public Mono<ServerResponse> clientDemo5(ServerRequest req) {
        //비동기 호출(비블록)
        Mono<List<GitHubDto>> values = webClient.get()
                .uri("http://www.naver.com")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<GitHubDto>>() {
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)).jitter(0.75));

        values.subscribe(System.out::println);

        return ServerResponse.ok().body(values, new ParameterizedTypeReference<>() {
        });
    }

    public RouterFunction<ServerResponse> clientRouterRule() {
        return RouterFunctions.route(RequestPredicates.GET("/client/get/1"), this::clientDemo1)
                .andRoute(RequestPredicates.GET("/client/get/2"), this::clientDemo2)
                .andRoute(RequestPredicates.GET("/client/get/3"), this::clientDemo3)
                .andRoute(RequestPredicates.GET("/client/get/4"), this::clientDemo4)
                .andRoute(RequestPredicates.GET("/client/get/5"), this::clientDemo5);

    }
}
