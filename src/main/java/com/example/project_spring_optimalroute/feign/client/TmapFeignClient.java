package com.example.project_spring_optimalroute.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.concurrent.CompletableFuture;


/*

Tmap API 제공 okhttp 예제코드

OkHttpClient client = new OkHttpClient();
MediaType mediaType = MediaType.parse("application/json");
RequestBody body = RequestBody.create(mediaType, "{\"tollgateFareOption\":16,\"roadType\":32,\"directionOption\":1,\"endX\":129.07579349764512,\"endY\":35.17883196265564,\"endRpFlag\":\"G\",\"reqCoordType\":\"WGS84GEO\",\"startX\":126.98217734415019,\"startY\":37.56468648536046,\"gpsTime\":\"20191125153000\",\"speed\":10,\"uncetaintyP\":1,\"uncetaintyA\":1,\"uncetaintyAP\":1,\"carType\":0,\"startName\":\"%EC%9D%84%EC%A7%80%EB%A1%9C%20%EC%9E%85%EA%B5%AC%EC%97%AD\",\"endName\":\"%ED%97%A4%EC%9D%B4%EB%A6%AC\",\"passList\":\"127.38454163183215,36.35127257501252\",\"gpsInfoList\":\"126.939376564495,37.470947057194365,120430,20,50,5,2,12,1_126.939376564495,37.470947057194365,120430,20,50,5,2,12,1\",\"detailPosFlag\":\"2\",\"resCoordType\":\"WGS84GEO\",\"sort\":\"index\"}");
Request request = new Request.Builder()
  .url("https://apis.openapi.sk.com/tmap/routes?version=1&callback=function")
  .post(body)
  .addHeader("accept", "application/json")
  .addHeader("content-type", "application/json")
  .addHeader("appKey", "e8wHh2tya84M88aReEpXCa5XTQf3xgo01aZG39k5")
  .build();

Response response = client.newCall(request).execute();



여기서 url ="https://apis.openapi.sk.com/tmap/routes?version=1&callback=function" 이걸 mapping을 내가 잘못하고 있는거 같아..
body params = 링크 참조 https://tmap-skopenapi.readme.io/reference/%EC%9E%90%EB%8F%99%EC%B0%A8-%EA%B2%BD%EB%A1%9C%EC%95%88%EB%82%B4
header = accept, contenttype = json, appkey는 일단 내 키로 넣었어 TmapFeignService 확인하면 돼

*/

@FeignClient(name = "tmap", url = "https://apis.openapi.sk.com", configuration = Config.class)
public interface TmapFeignClient {
    @PostMapping("/tmap/routes?version=1&callback=function") //@RequestMapping 도 가능
    CompletableFuture<String>/* 비동기식으로 구현하기 위한 CompletableFuture<> 사용 */ postRoute(
            // tmap api에서 요구하는 header 와 body 포함 메서드 생성
            @RequestHeader("accept") String accept,
            @RequestHeader("content-type") String contentType,
            @RequestHeader("appKey") String appKey,
            @RequestBody String request
    );
}


