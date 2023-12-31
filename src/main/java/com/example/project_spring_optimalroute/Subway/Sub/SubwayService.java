package com.example.project_spring_optimalroute.Subway.Sub;

import com.example.project_spring_optimalroute.Bus.Station.StationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubwayService {

    private final SubwayRepository subwayRepository;

    @Transactional(readOnly = true)
    public List<SubwayEntity> sub_testservice(){
        return subwayRepository.sub_testrespository();
    }

    @Transactional(readOnly = true)
    public List<SubwayEntity> sub_findByRadius(Double lng, Double lat){
        return subwayRepository.sub_findByRadius(lng,lat,3000);
    }
}
