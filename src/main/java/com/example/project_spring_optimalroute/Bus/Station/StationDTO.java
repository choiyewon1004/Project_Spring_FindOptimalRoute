package com.example.project_spring_optimalroute.Bus.Station;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StationDTO {
    private int stationinfo_code;
    private int stationinfo_ars;
    private String stationinfo_nm;
    private Double stationinfo_lng;
    private Double stationinfo_lat;

    public StationDTO(StationEntity entity){
        this.stationinfo_code = entity.getStationinfo_code();
        this.stationinfo_ars = entity.getStationinfo_ars();
        this.stationinfo_nm = entity.getStationinfo_nm();
        this.stationinfo_lng = entity.getStationinfo_lng();
        this.stationinfo_lat = entity.getStationinfo_lat();
    }
}
