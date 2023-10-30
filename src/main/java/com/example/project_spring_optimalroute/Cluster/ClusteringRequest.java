package com.example.project_spring_optimalroute.Cluster;

import com.example.project_spring_optimalroute.Route.RDTO;
import com.example.project_spring_optimalroute.exception.CustomIllegalArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClusteringRequest {
    double lat;
    double lon;
    RDTO locationInfo;


    public void validCheck(){

        if (lat < 0 || lat > 180 || lon < 0 || lon > 180){
            throw CustomIllegalArgumentException.message("유효한 좌표값을 입력해주세요.");
        }
    }
}