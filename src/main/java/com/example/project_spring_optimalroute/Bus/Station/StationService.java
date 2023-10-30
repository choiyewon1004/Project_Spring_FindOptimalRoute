package com.example.project_spring_optimalroute.Bus.Station;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StationService {
    private final StationRepository stationRepository;
    @Transactional(readOnly = true)
    public List<StationEntity> station_testservice(){
        return stationRepository.station_testrespository();
    }

    @Transactional(readOnly = true)
    public List<StationEntity> station_findByRadius(Double lng, Double lat){
        return stationRepository.station_findByRadius(lng,lat,100);
    }
}
