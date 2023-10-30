package com.example.project_spring_optimalroute.Bus.Stop;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StopDTO {
    private int stopinfo_code;
    private int stopinfo_pnum;
    private int stopinfo_bus;
    private int stopinfo_station;

    public StopDTO(StopEntity entity){
        this.stopinfo_code =entity.getStopinfo_code();
        this.stopinfo_pnum = entity.getStopinfo_pnum();
        this.stopinfo_bus = entity.getStopinfo_bus();
        this.stopinfo_station = entity.getStopinfo_station();
    }
}
