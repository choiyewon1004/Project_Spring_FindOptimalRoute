package com.example.project_spring_optimalroute.Bus.Stop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "stopinfo")
@Entity
public class StopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stopinfo_code;

    @Column
    private int stopinfo_pnum;

    @Column
    private int stopinfo_bus;

    @Column
    private int stopinfo_station;

}
