package com.example.project_spring_optimalroute.Controller;


import com.example.project_spring_optimalroute.API.BusApiClient;
import com.example.project_spring_optimalroute.API.TmapWebClient;
import com.example.project_spring_optimalroute.Cluster.ClusteringResult;
import com.example.project_spring_optimalroute.Cluster.GeoPoint;
import com.example.project_spring_optimalroute.Cluster.KmeansClusteringService;
import com.example.project_spring_optimalroute.Route.RDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.*;
import java.util.*;

@RequiredArgsConstructor
@Controller
public class backController {
//

    public Double start_lat = 37.45751315203417; public Double start_lng = 126.88862420186092;
    public Double end_lat = 37.55197466819207; public Double end_lng = 126.97323574278629 ;
    // 범위 단위를 알려주세요!
    public Integer set_radius = 1000;

    public int resultrdx = 1;
    public double resultnum = 99999.0;

    // kmeansclusterservice 객체 생성
    private final KmeansClusteringService kmeansClusteringService;

    // tmapwebclient 객체 생성
    private final TmapWebClient tmapWebClient;

    //busApiClient 객체 생성
    private final BusApiClient busApiClient;

    // 여기 url을 "/search/{startX}/{startY}/{endX}/{endY}" 로 하면 될 듯 합니다.
    @GetMapping("/")
    public String index(Model model){



        // 여기서 유저 입력 변수 선언하고
//        대충 이런 형식이었던 듯
//        startX = @PathVariable("startX" double startX)

//        tester
//        System.out.println("test");

        Map<String, Object> result = new HashMap<>();

        //func1
        ArrayList<RDTO> res1 = func1(start_lat, start_lng, end_lat, end_lng,set_radius);
        model.addAttribute("func1",res1);

        //func2
        List<ClusteringResult> res2 = func2(res1); // groupId, groupList 로 구성
//        이것도 tester 같아서 주석 처리했어
//        print_func2_data(res2);
//        id, [x,y], RDTO
//        System.out.println(res2.get(0).getGroupId());
        Map find_res2 = find_func2_middle(res2);
//        System.out.println(find_res2);
        int optimalClusterIdx = func3(find_res2);
        Map<Integer, List<Double>> targetCluster = new HashMap<>();
        for(int i = 0; i < res2.get(optimalClusterIdx).getClusteringLocationList().size(); i++){
            targetCluster.put(i, List.of(res2.get(optimalClusterIdx).getClusteringLocationList().get(i).getGeoPoint().getLat(),res2.get(optimalClusterIdx).getClusteringLocationList().get(i).getGeoPoint().getLon()));
        }
        int optimalPoint = func3(targetCluster);
//        System.out.println(targetCluster.get(optimalPoint));
        result = func4(targetCluster.get(optimalPoint));
        System.out.println(result);


//
//        처음에 작성한 병렬통신 결과 테스트
//        Mono apiTest = func3(find_res2);
//        apiTest.subscribe(result -> {
//            System.out.println(result);
//        },
//                error ->{
//                    System.err.println("에러 발생: " + error.toString());
//                });
//        System.out.println(apiTest);
////        리턴 값이 바뀌었기 때문에 주석 처리
//        List<RDTO> find_res2 = find_func2_middle(res2); // group 별 중심 info 정보, idx가 groupId
        model.addAttribute("func2",find_res2);

//        find_res2 객체 테스트
//        System.out.println(find_res2.size() + "\nfindResMid[0] Lat : " + find_res2.get(0).getRoute_lat() + "\nfindResMid[0] Lng : " + find_res2.get(0).getRoute_lng());


        //func3
        //int find_group = func3(find_res2);

        //func4
        //RDTO res_middle_point = func4(find_group, res2);

        return null;

    }

    /*
    기능 1
    - 목적지를 중심으로 반경을 설정하여 반경 내의 [정류소에 정차하는 버스 / 역에 정차하는 지하철  ]목록(reachable_list)
    - reachable_list에 존재하는 모든 [버스/지하철]이 운항하는 노선을 파악
    - 파악된 노선 중 출발지와 목적지 사이에 존재하는 [정류장 / 역]을 목록화(final_list)
    */
    public ArrayList<RDTO> func1(Double p_st, Double p_sg, Double p_et, Double p_eg, int p_set_radius){
        ArrayList<RDTO> res_list = new ArrayList<RDTO>();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://db.diligentp.com/Tagtag";
            String id = "tagtag";
            String pw = "tagtag";
            Connection conn = DriverManager.getConnection(url,id,pw);

            Double small_lat = p_st;
            Double big_lat = p_et;
            if(p_st > p_et){
                small_lat = p_et;big_lat = p_st ;
            }

            Double small_lng = p_sg;
            Double big_lng = p_eg;
            if(p_sg > p_eg){
                small_lng = p_eg;big_lng = p_sg ;
            }

            //bus
            String sql_bus =
                    "SELECT * FROM stationInfo " +
                            "WHERE (stationInfo_code IN( SELECT DISTINCT stopInfo_station FROM stopInfo WHERE stopInfo_bus IN ( SELECT stopInfo_bus FROM stopInfo WHERE stopInfo_station IN ( SELECT stationInfo_code FROM stationInfo WHERE ST_Distance_Sphere(POINT("+ p_eg +","+  p_et  +")," +
                            " POINT(stationInfo_lng, stationInfo_lat)) <= "+ p_set_radius+"))) AND (stationInfo_lng BETWEEN "+ small_lng +" AND " +big_lng+") AND (stationInfo_lat BETWEEN "+ small_lat +"AND " +big_lat+ "))";
            Statement stmt_bus = conn.createStatement();
            ResultSet rs_bus = stmt_bus.executeQuery(sql_bus);

//            while(rs_bus.next()) {
//                RDTO bean = new RDTO();
//                bean.setRoute_code(Integer.parseInt(rs_bus.getString("stationInfo_code")));
//                bean.setRoute_nm(rs_bus.getString("stationInfo_nm"));
//                bean.setRoute_lat(Double.parseDouble(rs_bus.getString("stationInfo_lat")));
//                bean.setRoute_lng(Double.parseDouble(rs_bus.getString("stationInfo_lng")));
//                bean.setRoute_type("bus");
//                res_list.add(bean);
//            }
            Set<String> coordinatesSet = new HashSet<>();

            while(rs_bus.next()) {
                String latLngKey = rs_bus.getString("stationInfo_lat") + "," + rs_bus.getString("stationInfo_lng");

                // 중복 체크
                if (!coordinatesSet.contains(latLngKey)) {
                    RDTO bean = new RDTO();
                    bean.setRoute_code(Integer.parseInt(rs_bus.getString("stationInfo_code")));
                    bean.setRoute_nm(rs_bus.getString("stationInfo_nm"));
                    bean.setRoute_lat(Double.parseDouble(rs_bus.getString("stationInfo_lat")));
                    bean.setRoute_lng(Double.parseDouble(rs_bus.getString("stationInfo_lng")));
                    bean.setRoute_type("bus");
                    res_list.add(bean);

                    // 중복 방지를 위해 HashSet에 좌표 추가
                    coordinatesSet.add(latLngKey);
                }
            }
//            tester
//            System.out.println(res_list.size());

            //subway
            String sql_subway =
                    "SELECT * FROM subInfo WHERE (subInfo_ho IN( SELECT subInfo_ho FROM subInfo WHERE ST_Distance_Sphere(POINT("+ p_eg +","+ p_et +"), POINT(subInfo_lng, subInfo_lat)) <= "+ p_set_radius+") AND (subInfo_lng BETWEEN "+ small_lng +" AND " +big_lng+") AND (subInfo_lat BETWEEN "+ small_lat +"AND " +big_lat+ "))";
            Statement stmt_subway = conn.createStatement();
            ResultSet rs_subway = stmt_subway.executeQuery(sql_subway);

            while(rs_subway.next()) {
                RDTO bean = new RDTO();
                bean.setRoute_code(Integer.parseInt(rs_subway.getString("subInfo_code")));
                bean.setRoute_nm(rs_subway.getString("subInfo_nm"));
                bean.setRoute_lat(Double.parseDouble(rs_subway.getString("subInfo_lat")));
                bean.setRoute_lng(Double.parseDouble(rs_subway.getString("subInfo_lng")));
                bean.setRoute_type("subway");
                res_list.add(bean);
            }
//            tester
//            System.out.println(res_list.size());

        }catch (Exception e){
            System.out.println(e);
        }


        return res_list;
    }



    /*
    기능 2
    - final_list의 위경도 값을 가져와 군집화
     */
    public List<ClusteringResult> func2(ArrayList<RDTO> list_data) {

        Map<GeoPoint, RDTO> data = make_data(list_data);
        List<ClusteringResult> res_func2 = kmeansClusteringService.getClusteringResult(data);
        return res_func2;
    }

    public Map<GeoPoint, RDTO> make_data(List<RDTO> dummy_data){
        final Map<GeoPoint, RDTO> GEO_POINT_MAP = new HashMap<>();

        for(int i=0;i<dummy_data.size();i++){
            GEO_POINT_MAP.put( GeoPoint.of(dummy_data.get(i).getRoute_lat(),dummy_data.get(i).getRoute_lng()) , dummy_data.get(i));
        }
//        tester
//        System.out.println(GEO_POINT_MAP);
        return GEO_POINT_MAP;
    }

    public void print_func2_data(List<ClusteringResult> res_func2){
        for(int i=0;i<res_func2.size();i++){
            System.out.println("log: << GroupID >>"+res_func2.get(i).getGroupId() + " :: size : "+ res_func2.get(i).getClusteringLocationList().size() + "::::::::::::::::::::::::::::::::::::::::");
            for(int j=0;j<4;j++){
                System.out.println("log: "+res_func2.get(i).getClusteringLocationList().get(j).getLocationInfo().getRoute_nm());

            }
        }
    }


    // 리턴 값 수정..
    public Map<Integer, List<Double>> /*List<RDTO>*/ find_func2_middle(List<ClusteringResult> res_func2) {
        List<RDTO> list_func2_middle = new ArrayList<>();

        for (int i = 0; i < res_func2.size(); i++) {
            List<ClusteringResult.ClusteringLocation> locations = res_func2.get(i).getClusteringLocationList();

            if (locations.isEmpty()) {
                // 위치 정보가 없는 군집은 스킵합니다.
                continue;
            }

            // 버블 정렬을 사용하여 위치 정보를 정렬합니다.
            bubbleSort(locations);

            // 중간 지점을 계산합니다.
            int middleIndex = locations.size() / 2;
            ClusteringResult.ClusteringLocation middleLocation = locations.get(middleIndex);

            // 중간 지점 정보를 RDTO에 저장합니다.
            RDTO middleRDTO = new RDTO();
            middleRDTO.setRoute_type(middleLocation.getLocationInfo().getRoute_type());
            middleRDTO.setRoute_nm(middleLocation.getLocationInfo().getRoute_nm());
            middleRDTO.setRoute_code(middleLocation.getLocationInfo().getRoute_code());
            middleRDTO.setRoute_lat(middleLocation.getGeoPoint().getLat());
            middleRDTO.setRoute_lng(middleLocation.getGeoPoint().getLon());

            list_func2_middle.add(middleRDTO);

            // 1. 완성된 클러스터의 인덱스를 추가한 데이터 프레임을 출력
            System.out.println("Cluster Index: " + res_func2.get(i).getGroupId() + "\nCluster Location Data SIZE : " + res_func2.get(i).getClusteringLocationList().size());
//            for (ClusteringResult.ClusteringLocation location : locations) {
//                System.out.println("Location: Lat=" + location.getGeoPoint().getLat() + ", Lon=" + location.getGeoPoint().getLon());
//            }

            // 2. 군집별로 경도와 위도의 평균 값을 인덱스와 함께 출력
            double totalLat = 0;
            double totalLon = 0;
            for (ClusteringResult.ClusteringLocation location : locations) {
                totalLat += location.getGeoPoint().getLat();
                totalLon += location.getGeoPoint().getLon();
            }

            // 지예야 여기 평균값을 구해놓았더구나... 역시 훌륭해..
            double avgLat = totalLat / locations.size();
            double avgLon = totalLon / locations.size();
//            System.out.println("Cluster " + res_func2.get(i).getGroupId() + " Average: Lat=" + avgLat + ", Lon=" + avgLon);
        }
        // 여기서 ClusterIDX : [MidLat, MidLng] 형식으로 해쉬맵 만들고 리턴하게 수정함
        // haspmap 객체 생성
        Map<Integer, List<Double>> resultMap = new HashMap<>();
        // for 문 돌려서 기존 res_func2 에 있는 idx 랑 idx에 해당하는 좌표값만 해쉬맵으로 생성
        // 기존 return 되는 친구는 KMeans 에서 제공되는 객체 그대로 dummy rdto 까지 가져오기 때문에 무겁다 생각
        for (int i = 0; i < list_func2_middle.size(); i++) {
            // i 번째 객체의 groupID 그냥 i로 넣었는데 맞겠지..? 코드리뷰 좀 부탁해요..
            int groupID = i;
            List<Double> coordinates = new ArrayList<>();
            // coordinates 리스트에 [위도, 경도] 순으로 넣었음
            coordinates.add(list_func2_middle.get(i).getRoute_lat());
            coordinates.add(list_func2_middle.get(i).getRoute_lng());
            //추가
            resultMap.put(groupID, coordinates);
        }
//        해쉬맵 테스트
//        System.out.println(resultMap);
        return resultMap;
    }

    private void bubbleSort(List<ClusteringResult.ClusteringLocation> locations) {
        int n = locations.size();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                ClusteringResult.ClusteringLocation location1 = locations.get(j);
                ClusteringResult.ClusteringLocation location2 = locations.get(j + 1);

                if (location1.getGeoPoint().getLat() > location2.getGeoPoint().getLat() ||
                        (location1.getGeoPoint().getLat() == location2.getGeoPoint().getLat() && location1.getGeoPoint().getLon() > location2.getGeoPoint().getLon())) {

                    // location1과 location2를 교환합니다.
                    locations.set(j, location2);
                    locations.set(j + 1, location1);
                    swapped = true;
                }
            }

            // 내부 루프에서 요소를 교환하지 않으면, 리스트는 이미 정렬된 상태입니다.
            if (!swapped) {
                break;
            }
        }
    }

    /*
    기능 3
    - API를 통해 출발점부터 군집 중심들까지의 택시 거리와 비용을 계산하여 가장 효율적인 군집 인덱스 도출
    */

    // 최적 클러스터 찾기
    // 리턴 값은 cluster id = integer
    // 인자는 func_2_mid 의 return 값과 clustering 된 전체
    public Integer func3(Map<Integer, List<Double>> clusterMidPoint){

        initCompare();
        // 출발지 좌표
        double startX = start_lng;
        double startY = start_lat;

        Map<Integer, Map<String, Double>> resultMap = new HashMap<>();

        for (int i = 0; i < clusterMidPoint.size(); i++){
            List<Double> searchPoint = clusterMidPoint.get(i);
//        System.out.println("clusterMidPoint.size() = " + clusterMidPoint.size());
//            System.out.println("searchPoint = " + searchPoint);
            double endX = searchPoint.get(1);
            double endY = searchPoint.get(0);
            int indexNum = i;
//            System.out.println(startX + "," + startY + "," + endX+","+endY);
            tmapWebClient.TmapWebClient(startX,startY,endX,endY)
                    .subscribe(result -> {

                        resultMap.put(indexNum, ComparingTaxiEff(result));
                        double tmp = resultMap.get(indexNum).get("farePerDistance");
                        if (tmp < resultnum){
                            resultnum = tmp;
                            resultrdx = indexNum;
                        }
//                        System.out.println("Result for " + indexNum + ":" + ComparingTaxiEff(result));
                    });

            try {
                Thread.sleep(1000); // 5초 지연
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        System.out.println(resultMap);
        return resultrdx;
    }

    public void initCompare() {
        resultnum = 99999.0;
        resultrdx = 0;
    }

    public Map<String, Double> ComparingTaxiEff(Map<String, Object> data) {

        //json parser
//        ObjectMapper objectMapper = new ObjectMapper();
        // result map
        ArrayList featuresList = (ArrayList) data.get("features");
        HashMap featuresMap = (HashMap) featuresList.get(0);
        HashMap propertiesMap = (HashMap) featuresMap.get("properties");

        Map<String, Double> efficiencyMap = new HashMap<>();

        try {
//            JsonNode jsonNode = objectMapper.readTree(rspString);

            // totalDistance, totalTime, taxiFare 추출
//            JsonNode propertiesNode = jsonNode.path("features").get(0).path("properties");
            // 총 거리 저장
            Integer totalDistance = (Integer) propertiesMap.get("totalDistance");
//            System.out.println(totalDistance);
//            int totalDistance = propertiesNode.path("totalDistance").asInt();
            // 총 시간
            Integer totalTime = (Integer) propertiesMap.get("totalTime");
//            System.out.println(totalTime);

//            int totalTime = propertiesNode.path("totalTime").asInt();
            // 택시 요금
            Integer taxiFare = (Integer) propertiesMap.get("taxiFare");
//            System.out.println(taxiFare);

//            int taxiFare = propertiesNode.path("taxiFare").asInt();

            // 거리당 소요 시간 계산 (소요 시간 / 거리)
            double timePerDistance = (double) totalTime / totalDistance;
            // 거리당 요금 계산 (요금 / 거리)
            double farePerDistance = (double) taxiFare / totalDistance;

            // Map에 저장
            efficiencyMap.put("timePerDistance", timePerDistance);
            efficiencyMap.put("farePerDistance", farePerDistance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return efficiencyMap;
    }











// 기존 코드
    //        List<List<Double>> coordinates = clusterMidPoint.values().stream()
//                .collect(Collectors.toList());
//        System.out.println(coordinates);
//        Flux<Map<String, Object>> parallelResults = Flux.fromIterable(clusterMidPoint.entrySet())
//                .parallel()
//                .runOn(Schedulers.parallel())
//                .flatMap(entry -> {
//                    Integer key = entry.getKey();
//                    List<Double> coordinate = entry.getValue();
//                    double endX = coordinate.get(0);
//                    double endY = coordinate.get(1);
//
//                    return tmapWebClient.TmapWebClient(startX, startY, endX, endY)
//                            .map(result -> {
//                                Map<String, Object> resultMap = new HashMap<>();
//                                resultMap.put("key", key);
//                                resultMap.put("result", result);
//                                return resultMap;
//                            });
//
//
//                });
//    }



    // 최적 클러스터 찾기
    // 리턴 값은 cluster id = integer
    // 좌표로 나와야 해서 int 말고 List로 받는게 나을 거야..
//        int res_len =0;
    // 1차 최적 클러스터 선정 병렬 통신 실패..
//        return callTmapWebClientParallel.callTmapWebClientParallel(startX, startY, clusterMidPoint)
//                .collectList()
//                .map(results -> {
//                    Map<Integer, Object> resultMap = new HashMap<>();
//                    for (int i = 0; i < results.size(); i++){
//                        resultMap.put(i, results.get(i));
//                    }
//                    return resultMap;
//                });


    /*
    기능 4
    - 가장 짧은 군집을 찾은 후 해당 군집에서 최적의 [정류장 / 역]을 찾기
     */
    public Map<String, Object> func4(List<Double> midPoint){
        double startX = start_lng;
        double startY = start_lat;
        double midX = midPoint.get(1);
        double midY = midPoint.get(0);
        double endX = end_lng;
        double endY = start_lat;
        Map<String, Object> resultMap = new HashMap<>();
//        Map<String, Map<Integer, Map<List<String>, List<List<Double>>>>> resultBusMap = new HashMap<>();
        tmapWebClient.TmapWebClient(startX,startY,midX,midY)
                .subscribe(result -> {
                    resultMap.put("taxi경로", func5(result));
//                        System.out.println("Result for " + indexNum + ":" + ComparingTaxiEff(result));
                });

        busApiClient.BusWebClient(midX,midY,endX,endY)
                .subscribe(result ->{
//                    System.out.println(result);
                    resultMap.put("bus경로", func5Bus(result));
                });

        return resultMap;
    }

    public List<List<Double>> func5(Map<String, Object> data) {
//  [[123.123,21414.123124],[123.123,21414.123124],[123.123,21414.123124],[123.123,21414.123124]]

        List<List<Double>> result = new ArrayList<>();

        ArrayList featuresList = (ArrayList) data.get("features");
        for(int i = 0; i < featuresList.size(); i++)
        {
            HashMap featuresMap = (HashMap) featuresList.get(i);
            HashMap geoMap = (HashMap) featuresMap.get("geometry");
//            System.out.println(geoMap);
            if (geoMap.get("type").equals("LineString")){
                ArrayList gpxList = (ArrayList) geoMap.get("coordinates");
                result.addAll(gpxList);
            }
        }

        return result;

    }

    /*
    기능 5
    - 최종적으로 출발지~[군집에서 찾은 최적의 위치]~목적지 로 경로가 구성
    */
    public Map<Integer, Map<List<String>, List<List<Double>>>> func5Bus(Map<String, Object> data) {
//  [[123.123,21414.123124],[123.123,21414.123124],[123.123,21414.123124],[123.123,21414.123124]]

        Map<Integer, Map<List<String>, List<List<Double>>>> result = new HashMap<>();

        Map<List<String>, List<List<Double>>> modeCoords = new HashMap<>();


        Map metaData = (HashMap) data.get("metaData");
//        System.out.println(metaData.getClass().getName());
        Map planMap = (HashMap) metaData.get("plan");
//        System.out.println(planMap.getClass().getName());
        ArrayList itineraries = (ArrayList) planMap.get("itineraries");
//        System.out.println(itineraries.getClass().getName());
//        System.out.println(itineraries.size());
        String route = new String();

        for (int i = 0; i < itineraries.size(); i ++){
            Map fareMap = (HashMap) itineraries.get(i);
            ArrayList legs = (ArrayList) fareMap.get("legs");
//            System.out.println(legs.size());
            for (int j =0; j < legs.size();j++){
//                result.put(i);
                Map step = (HashMap) legs.get(j);
//                System.out.println("step : " + step);
                String mode = (String) step.get("mode");
                if(mode.equals("WALK")){
                    route = "걷기";
                }
                else if (mode.equals("BUS")){
                    route = (String) step.get("route");
                }
                else if (mode.equals("SUBWAY")){
                    route = (String) step.get("route");
                }
//                System.out.println(mode);
                if(step.containsKey("steps")) {
                    ArrayList steps = (ArrayList) step.get("steps");
//                    System.out.println("try" + steps);
                    for (int k = 0; k < steps.size(); k++){
                        // steps 내 map 개체 홀드
                        Map stepsOb = (HashMap) steps.get(k);
                        // 개체 마다 존재하는 linestring
                        String lineString = (String) stepsOb.get("linestring");
                        // lineString split 해서 test_str 이게 리스트?
                        String[] test_str = lineString.split(" ");
                        List coords = new ArrayList<>();
                        for (int l = 0; l<test_str.length; l++){
                            coords.add(test_str[l].split(","));
                        }
                        modeCoords.put(List.of(mode, route), coords);
                    }
                }
                else if(step.containsKey("passShape")){
                    Map steps = (HashMap) step.get("passShape");
//                    System.out.println("Exception" + steps);
                    String lineString = (String) steps.get("linestring");
                    String[] test_str = lineString.split(" ");
                    List coords = new ArrayList<>();
                    for (int l = 0; l<test_str.length; l++){
                        coords.add(test_str[l].split(","));
                    }
                    modeCoords.put(List.of(mode, route), coords);


                }

//                Map stepsOb = (HashMap) steps.get(0);
//                String lineString = (String) stepsOb.get("linestring");
//                String[] test_str = lineString.split(" ");
////                System.out.println(test_str);
//                List coords = new ArrayList<>();
//                for (int k = 0; k<test_str.length; k++){
//                    coords.add(test_str[k].split(","));
//                }
//                System.out.println(coords);

            }
            result.put(i, modeCoords);
        }

// { "경로번호 i " : {j개수 만큼 "mode": [linestring(x,y)], "mode":[linestring(x,y)], }

//        ArrayList legsList = (ArrayList) stepsMap.get("legs");
//        System.out.println(legsList);
//        for(int i = 0; i < featuresList.size(); i++)
//        {
//            HashMap featuresMap = (HashMap) featuresList.get(i);
//            HashMap geoMap = (HashMap) featuresMap.get("geometry");
////            System.out.println(geoMap);
//            if (geoMap.get("type").equals("LineString")){
//                ArrayList gpxList = (ArrayList) geoMap.get("coordinates");
//                result.addAll(gpxList);
//            }
//        }

        return result;

    }

}


