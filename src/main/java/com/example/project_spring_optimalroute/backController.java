package com.example.project_spring_optimalroute;

import com.example.project_spring_optimalroute.Bus.Bus.BusDTO;
import com.example.project_spring_optimalroute.Bus.Station.StationDTO;
import com.example.project_spring_optimalroute.Route.RDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.*;
import java.util.ArrayList;

@RequiredArgsConstructor
@Controller
public class backController {

    public Double start_lat = 37.45751315203417; public Double start_lng = 126.88862420186092;
    public Double end_lat = 37.55197466819207; public Double end_lng = 126.97323574278629 ;
    public Integer set_radius = 3000;

    @GetMapping("/")
    public String index(Model model){
        ArrayList<RDTO> res1 =function1(start_lat, start_lng, end_lat, end_lng,set_radius);
        model.addAttribute("func1",res1);
        return "test";

    }

    /*
    기능 1
    - 목적지를 중심으로 반경을 설정하여 반경 내의 [정류소에 정차하는 버스 / 역에 정차하는 지하철  ]목록(reachable_list)
    - reachable_list에 존재하는 모든 [버스/지하철]이 운항하는 노선을 파악
    - 파악된 노선 중 출발지와 목적지 사이에 존재하는 [정류장 / 역]을 목록화(final_list)
    */
    public ArrayList<RDTO> function1(Double p_st, Double p_sg, Double p_et, Double p_eg, int p_set_radius){
        ArrayList<RDTO> res_list = new ArrayList<RDTO>();

        try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/testdb";
            String id = "testuser";
            String pw = "testuser";
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

