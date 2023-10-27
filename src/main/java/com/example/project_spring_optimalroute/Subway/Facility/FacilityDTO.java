package com.example.project_spring_optimalroute.Subway.Facility;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FacilityDTO {
    private int facilityinfo_code ;
    private String facilityinfo_ev ;
    private String facilityinfo_wh ;
    private String facilityinfo_parking ;
    private String facilityinfo_complain ;
    private String facilityinfo_exchange ;
    private String facilityinfo_train ;
    private String facilityinfo_culture ;
    private String facilityinfo_meeting ;
    private String facilityinfo_kids ;
    private int facilityinfo_sub ;

    public FacilityDTO (FacilityEntity entity){
        this.facilityinfo_code = entity.getFacilityinfo_code();
        this.facilityinfo_ev = entity.getFacilityinfo_ev();
        this.facilityinfo_wh = entity.getFacilityinfo_wh();
        this.facilityinfo_parking = entity.getFacilityinfo_parking();
        this.facilityinfo_complain = entity.getFacilityinfo_complain();
        this.facilityinfo_exchange = entity.getFacilityinfo_exchange();
        this.facilityinfo_train = entity.getFacilityinfo_train();
        this.facilityinfo_culture = entity.getFacilityinfo_culture();
        this.facilityinfo_meeting = entity.getFacilityinfo_meeting();
        this.facilityinfo_kids = entity.getFacilityinfo_kids();
        this.facilityinfo_sub = entity.getFacilityinfo_sub();
    }
}
