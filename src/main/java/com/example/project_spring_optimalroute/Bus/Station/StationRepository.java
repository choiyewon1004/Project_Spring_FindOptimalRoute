package com.example.project_spring_optimalroute.Bus.Station;

import com.example.project_spring_optimalroute.Subway.Sub.SubwayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StationRepository extends JpaRepository<StationEntity,Integer> {
    @Query(value ="SELECT * FROM stationinfo" ,nativeQuery = true)
    List<StationEntity> station_testrespository();
}
