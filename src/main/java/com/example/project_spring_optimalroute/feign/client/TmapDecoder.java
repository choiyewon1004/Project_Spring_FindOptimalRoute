package com.example.project_spring_optimalroute.feign.client;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;

// feign decoder api 호출 결과 log 로 확인하기 위해 생성
@Slf4j
public class TmapDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        if(response.status() == 200) {
            log.info("===============응답 200 정상===============");

            log.info("============Type 정보 -> ");
            log.info(type.getTypeName().toString());

            log.info("============Header 정보 -> ");
            log.info(response.headers().toString());
        }else{
            log.info("===============응답 확인필요" + String.valueOf(response.status()) + "===============");
        }

        return null;
    }
}
