package com.study.webfulx.handler;

import com.study.webfulx.config.WebClientConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(WebClientConfig.class)
@SpringBootTest(classes = TestHandler.class)
class TestHandlerTest2 {
    @Autowired
    TestHandler handler;

    @DisplayName("webMockServer Test")
    @Test
    public void test() {
        //given

        //when
        StepVerifier.create(handler.clientDemo6("055055"))
                .expectNextCount(30)
                .verifyComplete();
    }
}
