package com.example.project_spring_optimalroute.Subway.Facility;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacilityRepository extends JpaRepository<FacilityEntity, Integer> {
    @Query(value ="SELECT * FROM facilityinfo" ,nativeQuery = true)
    List<FacilityEntity> facil_testrespository();
}
