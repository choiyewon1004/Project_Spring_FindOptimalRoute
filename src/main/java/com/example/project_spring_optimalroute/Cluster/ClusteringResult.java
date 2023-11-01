package com.example.project_spring_optimalroute.Cluster;


import com.example.project_spring_optimalroute.Route.RDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ClusteringResult {
    Integer groupId;
    List<ClusteringLocation> clusteringLocationList;

    public static ClusteringResult of(Integer groupId, List<GeoPoint> geoPointList, Map<GeoPoint, RDTO> geoPointStringMap){
        ClusteringResult instance = new ClusteringResult();
        instance.groupId = groupId;
        List<ClusteringLocation> clusteringLocationList = new ArrayList<>();
        for(GeoPoint geoPoint : geoPointList) {
            clusteringLocationList.add(ClusteringLocation.of(geoPoint, geoPointStringMap.get(geoPoint)));
        }
        instance.clusteringLocationList = clusteringLocationList;
        return instance;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class ClusteringLocation {
        GeoPoint geoPoint;
        RDTO locationInfo;
    }


}