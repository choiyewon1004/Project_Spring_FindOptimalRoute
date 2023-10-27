package com.example.project_spring_optimalroute.Bus.Stop;

import com.example.project_spring_optimalroute.Subway.Sub.SubwayEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StopService {
    private final StopRepository stopRepository;

    @Transactional(readOnly = true)
    public List<StopEntity> stop_testservice(){
        return stopRepository.stop_testrespository();
    }
}
