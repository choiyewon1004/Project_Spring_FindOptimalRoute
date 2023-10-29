package com.example.project_spring_optimalroute.Subway.Sub;

import com.example.project_spring_optimalroute.Bus.Station.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubwayRepository extends JpaRepository<SubwayEntity, Integer> {
    @Query(value ="SELECT * FROM subinfo" ,nativeQuery = true)
    List<SubwayEntity> sub_testrespository();

    @Query(value ="SELECT *, ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(stationinfo_lng,stationinfo_lat))AS distance FROM subinfo WHERE ST_Distance_Sphere(POINT(:longitude, :latitude), POINT(stationinfo_lng, stationinfo_lat)) < :distanceLevel " ,nativeQuery = true)
    List<SubwayEntity> sub_findByRadius(@Param("longitude") Double longitude, @Param("latitude") Double latitude, @Param("distanceLevel") int distanceLevel );


}
