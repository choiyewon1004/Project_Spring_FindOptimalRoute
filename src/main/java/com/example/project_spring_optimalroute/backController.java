package com.example.project_spring_optimalroute;

import com.example.project_spring_optimalroute.Bus.Station.StationEntity;
import com.example.project_spring_optimalroute.Bus.Station.StationRepository;
import com.example.project_spring_optimalroute.Bus.Station.StationService;
import com.example.project_spring_optimalroute.Subway.Sub.SubwayEntity;
import com.example.project_spring_optimalroute.Subway.Sub.SubwayService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class backController {

    public Double start_lat = 37.55751315603417; public Double start_lng = 126.92518752070053;//출발지 : 홍대입구
    public Double end_lat = 37.528990711647076; public Double end_lng = 126.96656060319364 ;//목적지 : 용산역

    private final StationService stationService ;
    private final SubwayService subwayService;

    @GetMapping("/")
    public String index(Model model){

        return "test";

    }

    /*
    기능 1
    - 목적지를 중심으로 반경을 설정하여 반경 내의 [정류소에 정차하는 버스 / 역에 정차하는 지하철  ]목록(reachable_list)
    - reachable_list에 존재하는 모든 [버스/지하철]이 운항하는 노선을 파악
    - 파악된 노선 중 출발지와 목적지 사이에 존재하는 [정류장 / 역]을 목록화(final_list)
    */
    public List<StationEntity> function1(){
        List<StationEntity> func1_1_res =  stationService.station_findByRadius(end_lng,end_lat);
        return func1_1_res;
    }

    /*
    기능 2
    - final_list의 위경도 값을 가져와 군집화
     */

    /*
    기능 3
    - API를 통해 출발점부터 군집 중심들까지의 택시 거리와 비용을 계산
    */

    /*
    기능 4
    - 가장 짧은 군집을 찾은 후 해당 군집에서 최적의 [정류장 / 역]을 찾기
     */


    /*
    기능 5
    - 최종적으로 출발지~[군집에서 찾은 최적의 위치]~목적지 로 경로가 구성
     */
    

}

