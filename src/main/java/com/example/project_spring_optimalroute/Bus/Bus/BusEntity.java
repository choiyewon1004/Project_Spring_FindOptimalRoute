package com.example.project_spring_optimalroute.Bus.Bus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "busInfo")
@Entity
public class BusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int businfo_code;

    @Column(name = "busInfo_nm")
    private String businfo_nm;

    @Column(name = "busInfo_total")
    private int businfo_total;


}
