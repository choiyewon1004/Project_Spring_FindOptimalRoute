package com.example.project_spring_optimalroute.Controller;


import com.example.project_spring_optimalroute.Cluster.ClusteringResult;
import com.example.project_spring_optimalroute.Cluster.GeoPoint;
import com.example.project_spring_optimalroute.Cluster.KmeansClusteringService;
import com.example.project_spring_optimalroute.Route.RDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class backController {

    public Double start_lat = 37.45751315203417; public Double start_lng = 126.88862420186092;
    public Double end_lat = 37.55197466819207; public Double end_lng = 126.97323574278629 ;
    public Integer set_radius = 3000;

    private final KmeansClusteringService kmeansClusteringService ;

    @GetMapping("/")
    public String index(Model model){
        //func1
        ArrayList<RDTO> res1 =func1(start_lat, start_lng, end_lat, end_lng,set_radius);
        model.addAttribute("func1",res1);
        System.out.println(res1);

        //func2
        List<ClusteringResult> res2 = func2(res1); // groupId, groupList 로 구성
        //print_func2_data(res2);
        List<RDTO> find_res2 = find_func2_middle(res2); // group 별 중심 info 정보, idx가 groupId
        model.addAttribute("func2",find_res2);

        System.out.println(find_res2.size());
/*
        //func3
        int find_group = func3(find_res2);

        //func4
        //RDTO res_middle_point = func4(find_group, res2);
*/
        return "test";

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
//            String url = "jdbc:mysql://localhost:3306/testdb";
//            String id = "testuser";
//            String pw = "testuser";

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
//                    "SELECT bsi.* FROM busStationInfo AS bsi JOIN busInfo AS bi ON bsi.busInfo_code = bi.busInfo_code WHERE bi.busInfo_nm IN ( SELECT busInfo_nm FROM busInfo WHERE busInfo_code IN ( SELECT DISTINCT stopInfo_bus FROM stopInfo WHERE stopInfo_station IN (SELECT stationInfo_code FROM stationInfo WHERE ST_Distance_Sphere(POINT("+ p_eg +","+  p_et  +"), POINT(stationInfo_lng, stationInfo_lat)) <= 500))";
                    "SELECT * FROM stationinfo WHERE (stationInfo_code IN( SELECT DISTINCT stopInfo_station FROM stopinfo WHERE stopInfo_bus IN ( SELECT stopInfo_bus FROM stopinfo WHERE stopinfo_station IN ( SELECT stationInfo_code FROM stationinfo WHERE ST_Distance_Sphere(POINT("+ p_eg +","+  p_et  +"), POINT(stationInfo_lng, stationInfo_lat)) <= "+ p_set_radius+"))) AND (stationinfo_lng BETWEEN "+ small_lng +" AND " +big_lng+") AND (stationinfo_lat BETWEEN "+ small_lat +"AND " +big_lat+ "))";
            Statement stmt_bus = conn.createStatement();
            ResultSet rs_bus = stmt_bus.executeQuery(sql_bus);

            while(rs_bus.next()) {
                RDTO bean = new RDTO();
                bean.setRoute_code(Integer.parseInt(rs_bus.getString("stationinfo_code")));
                bean.setRoute_nm(rs_bus.getString("stationinfo_nm"));
                bean.setRoute_lat(Double.parseDouble(rs_bus.getString("stationinfo_lat")));
                bean.setRoute_lng(Double.parseDouble(rs_bus.getString("stationinfo_lng")));
                bean.setRoute_type("bus");
                res_list.add(bean);
            }

            System.out.println(res_list.size());
            System.out.println(res_list);

            //subway
            String sql_subway =
//                    "SELECT *, ST_Distance_Sphere(POINT("+ p_eg +","+  p_et  +"), POINT(subInfo_lng, subInfo_lat)) AS distance FROM subInfo WHERE ST_Distance_Sphere(POINT("+ p_eg +","+  p_et  +"), POINT(subInfo_lng, subInfo_lat)) <= 500 ORDER BY distance";
                    "SELECT * FROM subinfo WHERE (subinfo_ho IN( SELECT subinfo_ho FROM subinfo WHERE ST_Distance_Sphere(POINT("+ p_eg +","+  p_et  +"), POINT(subinfo_lng, subinfo_lat)) <= "+ p_set_radius+") AND (subinfo_lng BETWEEN "+ small_lng +" AND " +big_lng+") AND (subinfo_lat BETWEEN "+ small_lat +"AND " +big_lat+ "))";

            Statement stmt_subway = conn.createStatement();
            ResultSet rs_subway = stmt_subway.executeQuery(sql_subway);

            while(rs_subway.next()) {
                RDTO bean = new RDTO();
                bean.setRoute_code(Integer.parseInt(rs_subway.getString("subinfo_code")));
                bean.setRoute_nm(rs_subway.getString("subinfo_nm"));
                bean.setRoute_lat(Double.parseDouble(rs_subway.getString("subinfo_lat")));
                bean.setRoute_lng(Double.parseDouble(rs_subway.getString("subinfo_lng")));
                bean.setRoute_type("subway");
                res_list.add(bean);
            }

            System.out.println(res_list.size());
            System.out.println(res_list);
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


    public List<RDTO> find_func2_middle(List<ClusteringResult> res_func2) {
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
            middleRDTO.setRoute_lat(middleLocation.getGeoPoint().getLat());
            middleRDTO.setRoute_lng(middleLocation.getGeoPoint().getLon());

            list_func2_middle.add(middleRDTO);

            // 1. 완성된 클러스터의 인덱스를 추가한 데이터 프레임을 출력
            System.out.println("Cluster Index: " + res_func2.get(i).getGroupId());
            for (ClusteringResult.ClusteringLocation location : locations) {
                System.out.println("Location: Lat=" + location.getGeoPoint().getLat() + ", Lon=" + location.getGeoPoint().getLon());
            }

            // 2. 군집별로 경도와 위도의 평균 값을 인덱스와 함께 출력
            double totalLat = 0;
            double totalLon = 0;
            for (ClusteringResult.ClusteringLocation location : locations) {
                totalLat += location.getGeoPoint().getLat();
                totalLon += location.getGeoPoint().getLon();
            }
            double avgLat = totalLat / locations.size();
            double avgLon = totalLon / locations.size();
            System.out.println("Cluster " + res_func2.get(i).getGroupId() + " Average: Lat=" + avgLat + ", Lon=" + avgLon);
        }

        return list_func2_middle;
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
    public int func3(List<RDTO> res_func3){
        int res_closer =99999999;
        int res_idx =0;
        for(int i=0;i<res_func3.size();i++){
            int here_length = find_length(res_func3.get(i));
            if(here_length < res_closer){
                res_closer = here_length;
                res_idx = i;
            }
        }

        return res_idx;
    }

    // 출발점과 환승지(find_point)까지의 거리 비용 계산
    public int /*List<Contributor>*/ find_length(RDTO find_point){
        // 좌표로 나와야 해서 int 말고 List로 받는게 나을 거야..
        int res_len =0;

        /* 원래 코드..
        public class TmapFeignController {
            private final TmapFeignService tmapFeignService;

            @Autowired
            public TmapFeignController(TmapFeignService tmapFeignService){
                this.tmapFeignService = tmapFeignService;
            }

            @GetMapping("/search/{x}/{y}") //Get, Post 둘 다 해봐도 404..
            // 여기서 pathvariable 을 startX, startY 는 유저에게서 받고 endX, endY 는 클러스터의 중심점으로 해야됨
            // test 중 endX,endY는 고정으로 진행
            public CompletableFuture<String> search(@PathVariable("x") double x, @PathVariable("y") double y) {
                CompletableFuture<String> result = tmapFeignService.fetchRouteData(x, y);
                return result;
            }
        }
        */

        return res_len;
    }

    /*
    기능 4
    - 가장 짧은 군집을 찾은 후 해당 군집에서 최적의 [정류장 / 역]을 찾기
     */

    public RDTO func4(int find_idx, List<ClusteringResult> find_clustering){
        RDTO res4 = new RDTO();
        ClusteringResult find_cluster = find_clustering.get(find_idx);

        int res_closer = 999999;
        int res_idx =0;

        for(int i=0;i<find_cluster.getClusteringLocationList().size();i++){
            int here_length = find_length(find_cluster.getClusteringLocationList().get(i).getLocationInfo());
            if(here_length < res_closer){
                res_closer = here_length;
                res_idx = i;
            }
        }

        res4 = find_cluster.getClusteringLocationList().get(res_idx).getLocationInfo();
        return res4;
    }

    /*
    기능 5
    - 최종적으로 출발지~[군집에서 찾은 최적의 위치]~목적지 로 경로가 구성
    */
    

}

