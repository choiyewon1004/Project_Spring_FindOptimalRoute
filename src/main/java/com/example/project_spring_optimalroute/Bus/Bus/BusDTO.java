package com.example.project_spring_optimalroute.Bus.Bus;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BusDTO {

    private int businfo_code;
    private String businfo_nm;
    private int businfo_total;

    public BusDTO(BusEntity entity){
        this.businfo_code = entity.getBusinfo_code();
        this.businfo_nm = entity.getBusinfo_nm();
        this.businfo_total = entity.getBusinfo_total();
    }

}
