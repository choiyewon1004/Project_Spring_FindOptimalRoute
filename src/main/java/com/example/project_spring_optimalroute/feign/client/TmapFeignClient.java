package com.example.project_spring_optimalroute.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.concurrent.CompletableFuture;

@FeignClient(name = "tmap", url = "https://apis.openapi.sk.com", configuration = Config.class)
public interface TmapFeignClient {
    @PostMapping("/tmap/routes?version=1&callback=function")
    CompletableFuture<String>postRoute(
            @RequestHeader("accept") String accept,
            @RequestHeader("content-type") String contentType,
            @RequestHeader("appKey") String appKey,
            @RequestBody String request
    );
}


