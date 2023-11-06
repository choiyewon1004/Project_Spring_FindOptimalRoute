package com.example.project_spring_optimalroute.Bus.Stop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "stopInfo")
@Entity
public class StopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stopinfo_code;

    @Column(name = "stopInfo_pnum")
    private int stopinfo_pnum;

    @Column(name = "stopInfo_bus")
    private int stopinfo_bus;

    @Column(name = "stopInfo_station")
    private int stopinfo_station;

}
