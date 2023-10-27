package com.example.project_spring_optimalroute.Bus.Stop;

import com.example.project_spring_optimalroute.Subway.Sub.SubwayEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StopRepository extends JpaRepository<StopEntity, Integer> {
    @Query(value ="SELECT * FROM stopinfo" ,nativeQuery = true)
    List<StopEntity> stop_testrespository();
}
