package com.example.project_spring_optimalroute.Cluster;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GeoPoint implements Serializable {
    private double lat;
    private double lon;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GeoPoint geoPoint = (GeoPoint) o;
        return Double.compare(simple(geoPoint.lat), simple(lat)) == 0 &&
                Double.compare(simple(geoPoint.lon), simple(lon)) == 0;
    }

    private Double simple(double val) {
        int valid = 10000000;
        return Math.floor(val * valid) / (double) valid;
    }
}