package com.example.project_spring_optimalroute.Subway.Facility;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FacilityService {
    private final FacilityRepository facilityRepository;

    @Transactional(readOnly = true)
    public List<FacilityEntity> facil_testservice(){
        return facilityRepository.facil_testrespository();
    }
}
