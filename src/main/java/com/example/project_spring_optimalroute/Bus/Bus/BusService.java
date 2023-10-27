package com.example.project_spring_optimalroute.Bus.Bus;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BusService {
    private final BusRepository busRepository;
    @Transactional(readOnly = true)
    public List<BusEntity> bus_testservice(){
        return busRepository.bus_testrespository();
    }

}
