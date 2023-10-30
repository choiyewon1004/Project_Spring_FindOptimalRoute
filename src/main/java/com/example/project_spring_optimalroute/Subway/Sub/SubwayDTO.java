package com.example.project_spring_optimalroute.Subway.Sub;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubwayDTO {
    private int subinfo_code;
    private String subinfo_nm;
    private Double subinfo_lat;
    private Double subinfo_lng;
    private String subinfo_ho;

    public SubwayDTO (SubwayEntity entity ){
        this.subinfo_code = entity.getSubinfo_code();
        this.subinfo_nm = entity.getSubinfo_nm();
        this.subinfo_lat = entity.getSubinfo_lat();
        this.subinfo_lng = entity.getSubinfo_lng();
        this.subinfo_ho = entity.getSubinfo_ho();
    }
}
