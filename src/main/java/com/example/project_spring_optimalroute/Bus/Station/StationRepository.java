package com.example.project_spring_optimalroute.Bus.Station;

import com.example.project_spring_optimalroute.Subway.Sub.SubwayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StationRepository extends JpaRepository<StationEntity,Integer> {
    @Query(value ="SELECT * FROM stationinfo" ,nativeQuery = true)
    List<StationEntity> station_testrespository();

    @Query(value ="SELECT * FROM stationinfo WHERE ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(stationinfo_lng, stationinfo_lat)) < :distanceLevel " ,nativeQuery = true)
    List<StationEntity> station_findByRadius(@Param("longitude") Double longitude, @Param("latitude") Double latitude, @Param("distanceLevel") int distanceLevel );


}
