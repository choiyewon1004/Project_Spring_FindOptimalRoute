package com.example.project_spring_optimalroute.Subway.Facility;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "facilityinfo")
@Entity
public class FacilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int facilityinfo_code ;
    @Column
    private String facilityinfo_ev ;
    @Column
    private String facilityinfo_wh ;
    @Column
    private String facilityinfo_parking ;
    @Column
    private String facilityinfo_complain ;
    @Column
    private String facilityinfo_exchange ;
    @Column
    private String facilityinfo_train ;
    @Column
    private String facilityinfo_culture ;
    @Column
    private String facilityinfo_meeting ;
    @Column
    private String facilityinfo_kids ;
    @Column
    private int facilityinfo_sub ;

}
