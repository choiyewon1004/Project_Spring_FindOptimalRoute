package com.example.project_spring_optimalroute.Subway.Sub;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "subinfo")
@Entity
public class SubwayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subinfo_code;

    @Column
    private String subinfo_nm;

    @Column
    private Double subinfo_lat;

    @Column
    private Double subinfo_lng;

    @Column
    private String subinfo_ho;


}
