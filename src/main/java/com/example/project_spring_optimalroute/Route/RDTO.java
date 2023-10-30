package com.example.project_spring_optimalroute.Route;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RDTO {

    private int route_code;
    private String route_nm;
    private Double route_lng;
    private Double route_lat;
    private String route_type;

}
