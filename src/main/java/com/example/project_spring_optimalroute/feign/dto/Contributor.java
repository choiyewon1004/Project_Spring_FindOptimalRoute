package com.example.project_spring_optimalroute.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

// 일단 response 온다는 기준으로 dto 만들었는데 이게 맞는지 모르겟다..
@Data
public class Contributor {
    private String login;
    private int contributions;

    @Data
    public static class RouteResponse {
        private String type;
        private List<Feature> features;

        @Data
        public static class Feature {
            private String type;
            private Geometry geometry;
            private Properties properties;
        }

        @Data
        public static class Geometry {
            private String type;
            private List<Double> coordinates;
        }

        @Data
        public static class Properties {
            private int totalDistance;
            private int totalTime;
            private int totalFare;
            private int taxiFare;
            private int index;
            private int pointIndex;
            private String name;
            private String description;
            private String nextRoadName;
            private int turnType;
            private String pointType;
            private int lineIndex;
            private int distance;
            private int time;
            private int roadType;
            private int facilityType;
        }
    }
}
