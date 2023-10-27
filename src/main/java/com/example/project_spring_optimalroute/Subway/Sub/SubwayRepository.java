package com.example.project_spring_optimalroute.Subway.Sub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubwayRepository extends JpaRepository<SubwayEntity, Integer> {
    @Query(value ="SELECT * FROM subinfo" ,nativeQuery = true)
    List<SubwayEntity> sub_testrespository();

}
