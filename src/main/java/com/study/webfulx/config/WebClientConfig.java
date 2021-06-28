package com.study.webfulx.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1500)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(1500, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(1500, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .filters(functions -> {
                    functions.add(logRequest());
                    functions.add(logResponse());
                })
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }


    public ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));

            return next.exchange(clientRequest);
        };
    }

    public ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response: {}", clientResponse.headers().asHttpHeaders().get("property-header"));
            return Mono.just(clientResponse);
        });
    }
}
