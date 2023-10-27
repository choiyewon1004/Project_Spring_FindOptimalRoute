package com.example.project_spring_optimalroute.Bus.Station;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "stationinfo")
@Entity
public class StationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stationinfo_code;

    @Column
    private int stationinfo_ars;

    @Column
    private String stationinfo_nm;

    @Column
    private Double stationinfo_lng;

    @Column
    private Double stationinfo_lat;


}