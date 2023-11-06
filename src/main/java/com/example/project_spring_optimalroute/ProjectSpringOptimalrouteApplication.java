package com.example.project_spring_optimalroute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = "com.example.project_spring_optimalroute.feign.client")
public class ProjectSpringOptimalrouteApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectSpringOptimalrouteApplication.class, args);
    }

}
