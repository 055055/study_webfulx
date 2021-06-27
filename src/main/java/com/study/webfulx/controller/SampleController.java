package com.study.webfulx.controller;

import com.study.webfulx.utils.ConvertUtil;
import com.study.webfulx.utils.Output;
import com.study.webfulx.utils.SampleDto;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequestMapping("/sample")
@RestController
public class SampleController {

    /**
     * 인수 없이 문자열 반환
     *
     * @return
     */
    @GetMapping("/1")
    public Mono<String> sample1() {
        return Mono.just("Hello Annotation WebFlux World");
    }

    /**
     * URL에 인수가 있는 경우
     *
     * @param name
     * @return
     */
    @GetMapping("/2/{name}")
    public Mono<String> sample2(@PathVariable("name") String name) {
        return Mono.just(("Hello " + name));
    }

    /**
     * 쿼리 스트릥에 인수가 있는 경우
     *
     * @param name
     * @return
     */
    @GetMapping("/3")
    public Mono<String> sample3(@RequestParam("name") String name) {
        return Mono.just("Hello ! " + name);
    }

    /**
     * 모든 쿼리 스트링을 취득하는 경우
     *
     * @param req
     * @return
     */
    @GetMapping("/4")
    public Mono<Map<String, String>> sample4(@RequestParam Map<String, String> req) {
        return Mono.just(req);
    }

    /**
     * 송신된 데이터를 문자열로 취급해 그대로 반환하는 경우
     *
     * @param req
     * @return
     */
    @PostMapping("/5")
    public Mono<String> handle1(@RequestBody String req) {
        return Mono.just(req);
    }

    /**
     * x-www-form-urlencode에 의해 폼이 송신된 경우
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/6")
    public Mono<MultiValueMap<String, String>> handle2(@RequestBody MultiValueMap<String, String> req) {
        return Mono.just(req);
    }

    /**
     * Body에 JSON이 송신된 경우
     *
     * @param req
     * @return
     */
    @PostMapping("/7")
    public Mono<Output> handle3(@RequestBody SampleDto req) {
        return Mono.just(ConvertUtil.convert(req));
    }
}
