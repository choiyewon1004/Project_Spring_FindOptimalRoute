package com.example.project_spring_optimalroute.Subway.Facility;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "facilityInfo")
@Entity
public class FacilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int facilityinfo_code ;

    @Column(name = "facilityInfo_ev")
    private String facilityinfo_ev ;
    @Column(name = "facilityInfo_wh")
    private String facilityinfo_wh ;
    @Column(name = "facilityInfo_parking")
    private String facilityinfo_parking ;
    @Column(name = "facilityInfo_complain")
    private String facilityinfo_complain ;
    @Column(name = "facilityInfo_exchange")
    private String facilityinfo_exchange ;
    @Column(name = "facilityInfo_train")
    private String facilityinfo_train ;
    @Column(name = "facilityInfo_culture")
    private String facilityinfo_culture ;
    @Column(name = "facilityInfo_meeting")
    private String facilityinfo_meeting ;
    @Column(name = "facilityInfo_kids")
    private String facilityinfo_kids ;
    @Column(name = "facilityInfo_sub")
    private int facilityinfo_sub ;

}
