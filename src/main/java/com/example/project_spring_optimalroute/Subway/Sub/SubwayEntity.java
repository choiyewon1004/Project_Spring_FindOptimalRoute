package com.example.project_spring_optimalroute.Subway.Sub;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "subInfo")
@Entity
public class SubwayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subInfo_code")
    private int subinfo_code;

    @Column(name = "subInfo_nm")
    private String subinfo_nm;

    @Column(name = "subInfo_lat")
    private Double subinfo_lat;

    @Column(name = "subInfo_lng")
    private Double subinfo_lng;

    @Column(name = "subInfo_ho")
    private String subinfo_ho;


}
