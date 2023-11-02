package com.example.project_spring_optimalroute.feign.service;

import com.example.project_spring_optimalroute.feign.client.TmapFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TmapFeignService {
    private final TmapFeignClient tmapFeignClient;

    @Autowired
    public TmapFeignService(TmapFeignClient tmapFeignClient) {this.tmapFeignClient = tmapFeignClient; }

    public CompletableFuture<String> fetchRouteData(double x, double y) {
        String accept = "application/json";
        String contentType = "application/json";
        String appKey = "laYUuF3qRu5hqtujYlNlz6lGfbDecGVoahDvH3yF";

        String request = String.format("{\"tollgateFareOption\":16,\"roadType\":32,\"directionOption\":1,\"endX\":129.07579349764512,\"endY\":35.17883196265564,\"endRpFlag\":\"G\",\"reqCoordType\":\"WGS84GEO\",\"startX\":%.14f,\"startY\":%.14f,\"gpsTime\":\"20191125153000\",\"speed\":10,\"uncetaintyP\":1,\"uncetaintyA\":1,\"uncetaintyAP\":1,\"carType\":0,\"detailPosFlag\":\"2\",\"resCoordType\":\"WGS84GEO\",\"sort\":\"index\"}", x, y);

        return tmapFeignClient.postRoute(accept, contentType, appKey, request);
    }

}
