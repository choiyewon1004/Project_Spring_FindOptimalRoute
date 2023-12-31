package com.example.project_spring_optimalroute.API;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class BusApiClient {

    // application.properties 에서 가져와서 선언
    @Value("${api.baseurl}")
    private String baseUrl;


    @Value("${api.appkey}")
    private String appKey;

    private HttpClient httpClient;

    private ExchangeStrategies exchangeStrategies;

    @PostConstruct
    public void initSetting() {
        httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }

    //     이 뒤에 클러스터된 데이터도 리스트로 같이 들어와야함 직렬방식 통신 코드
    public Mono<Map<String, Object>> BusWebClient(double startX, double startY, double endX, double endY) {
        // call test
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .build()
                .post()
                .uri("transit/routes?version=1&callback=function")
                .header("accept", "application/json")
                .header("appKey", appKey)
                .body(BodyInserters.fromValue(String.format("{\"startX\":\"%.14f\",\"startY\":\"%.14f\",\"endX\":\"%.14f\",\"endY\":\"%.14f\",\"lang\":0,\"format\":\"json\",\"count\":10}", startX, startY, endX, endY)))
                .retrieve()
                .bodyToMono(String.class)
                .map(jsonString -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
                        return objectMapper.readValue(jsonString, typeReference);
                    } catch (Exception e) {
                        log.error("JSON 파싱 중 오류 발생: " + e.getMessage());
                        throw new RuntimeException("JSON 파싱 중 오류 발생: " + e.getMessage());
                    }
                })
                .onErrorResume(throwable -> {
                    log.error("에러 : " + throwable.getMessage());
                    return Mono.empty();
                });
    }
}

