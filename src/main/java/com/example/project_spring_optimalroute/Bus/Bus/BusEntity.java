package com.example.project_spring_optimalroute.Bus.Bus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "businfo")
@Entity
public class BusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int businfo_code;

    @Column
    private String businfo_nm;

    @Column
    private int businfo_total;


}
