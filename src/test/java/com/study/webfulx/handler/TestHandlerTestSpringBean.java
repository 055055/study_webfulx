package com.study.webfulx.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.webfulx.config.TestWebClientConfig;
import com.study.webfulx.utils.GitHubDto;
import mockwebserver3.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.study.webfulx.config.TestWebClientConfig.mockWebServer;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(TestWebClientConfig.class)
@SpringBootTest(classes = TestHandler.class)
class TestHandlerTestSpringBean {

    @Autowired
    TestHandler handler;

    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("webMockServer Test")
    @Test
    public void test() {
        //given
        mockWebServer.enqueue(
                new MockResponse().setResponseCode(400)
        );

        //when
        StepVerifier.create(handler.clientDemo6("055055"))
                .expectError();

    }

    @DisplayName("webMockServer Test- 200")
    @Test
    public void test_200() throws JsonProcessingException {
        //given
        GitHubDto dto1 = new GitHubDto();
        dto1.setId("1622941913451");
        dto1.setFullName("055055/bank");
        dto1.setIssueCount(0);

        GitHubDto dto2 = new GitHubDto();
        dto2.setId("1600580264324");
        dto2.setFullName("055055/market");
        dto2.setIssueCount(0);


        GitHubDto dto3 = new GitHubDto();
        dto3.setId("444423408312312");
        dto3.setFullName("055055/user");
        dto3.setIssueCount(0);

        List<GitHubDto> gitHubDtoList = Arrays.asList(dto1, dto2, dto3);
        String response = objectMapper.writeValueAsString(gitHubDtoList);

        mockWebServer.enqueue(
                new MockResponse().setBody(response)
                        .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)

        );

        //when
        StepVerifier.create(handler.clientDemo6("055055"))
                .assertNext(result -> {
                            assertThat(result.getId()).isEqualTo(dto1.getId());
                            assertThat(result.getFullName()).isEqualTo(dto1.getFullName());
                            assertThat(result.getIssueCount()).isEqualTo(dto1.getIssueCount());
                        }
                )
                .assertNext(result -> {
                            assertThat(result.getId()).isEqualTo(dto2.getId());
                            assertThat(result.getFullName()).isEqualTo(dto2.getFullName());
                            assertThat(result.getIssueCount()).isEqualTo(dto2.getIssueCount());
                        }
                )
                .assertNext(result -> {
                            assertThat(result.getId()).isEqualTo(dto3.getId());
                            assertThat(result.getFullName()).isEqualTo(dto3.getFullName());
                            assertThat(result.getIssueCount()).isEqualTo(dto3.getIssueCount());
                        }
                )
                .verifyComplete();
    }
}
