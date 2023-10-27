package com.example.project_spring_optimalroute.Bus.Bus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusRepository extends JpaRepository<BusEntity, Integer> {
    @Query(value ="SELECT * FROM businfo" ,nativeQuery = true)
    List<BusEntity> bus_testrespository();
}
