package com.example.project_spring_optimalroute.Bus.Station;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "stationInfo")
@Entity
public class StationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stationinfo_code;

    @Column(name = "stationInfo_ars")
    private int stationinfo_ars;

    @Column(name = "stationInfo_nm")
    private String stationinfo_nm;

    @Column(name = "stationInfo_lng")
    private Double stationinfo_lng;

    @Column(name = "stationInfo_lat")
    private Double stationinfo_lat;


}