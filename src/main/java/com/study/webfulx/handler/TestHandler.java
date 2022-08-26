package com.study.webfulx.handler;

import com.study.webfulx.utils.GitHubDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Configuration
@RequiredArgsConstructor
public class TestHandler {
    private final WebClient testWebClient;

    public Flux<GitHubDto> clientDemo6(String userName) {
        if (!StringUtils.hasText(userName)) {
            throw new RuntimeException("user is Required");
        }

        Flux<GitHubDto> values = testWebClient.get()
                .uri("/users/{name}/repos", userName)
                .retrieve()
                .bodyToFlux(GitHubDto.class);
        return values;
    }
}
