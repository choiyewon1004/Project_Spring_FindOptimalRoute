package com.example.project_spring_optimalroute.feign.client;

import feign.Logger;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// API 호출 후 로그 확인하는 건데 구현만 되면 없어도 돼..
@Configuration
public class Config {
    @Bean
    Logger.Level feignLoggerLevel() { return Logger.Level.FULL; }
    @Bean
    Decoder feignDecoder() { return new TmapDecoder();}
}
