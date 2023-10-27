package com.example.project_spring_optimalroute.Bus.Station;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StationService {
    private final StationRepository stationRepository;
    @Transactional(readOnly = true)
    public List<StationEntity> station_testservice(){
        return stationRepository.station_testrespository();
    }
}
