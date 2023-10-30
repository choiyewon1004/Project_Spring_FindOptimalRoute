package com.example.project_spring_optimalroute.Cluster;


import com.example.project_spring_optimalroute.Route.RDTO;
import com.example.project_spring_optimalroute.exception.CustomIllegalArgumentException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import smile.clustering.KMeans;
import smile.clustering.PartitionClustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KmeansClusteringServiceImpl implements KmeansClusteringService {
    int kk = 20;

    @Override
    public List<ClusteringResult> getClusteringResult(Map<GeoPoint, RDTO> GEO_POINT_MAP){
        List<GeoPoint> geoPointList =  new ArrayList<>(GEO_POINT_MAP.keySet());
        double[][] geoPointArray = getGeoPointArray(geoPointList);

        int numberOfGeoPoints = geoPointList.size();
        if (2 < numberOfGeoPoints && numberOfGeoPoints <= 10) {
            kk = 3;
        } else if (10 < numberOfGeoPoints && numberOfGeoPoints <= 20) {
            kk = 4;
        } else if (20 < numberOfGeoPoints && numberOfGeoPoints <= 30) {
            kk = 5;
        } else if (numberOfGeoPoints > 30) {
            kk = 6;
        }

        KMeans clusters = PartitionClustering.run(20, () -> KMeans.fit(geoPointArray, kk));
        Map<Integer, List<GeoPoint>> groupIdGeoPointMap = new HashMap<>();
        for (int i = 0, yLength = clusters.y.length; i < yLength; i++) {
            int groupId = clusters.y[i];
            GeoPoint geoPoint = geoPointList.get(i);
            if(geoPoint != null){
                groupIdGeoPointMap.computeIfAbsent(groupId, k -> new ArrayList()).add(geoPoint);
            }
        }
        List<ClusteringResult> clusteringResultList = new ArrayList<>();
        for(Map.Entry<Integer, List<GeoPoint>> entry : groupIdGeoPointMap.entrySet()){
            clusteringResultList.add(ClusteringResult.of(entry.getKey(), entry.getValue(), GEO_POINT_MAP));
        }
        return clusteringResultList;
    }

    @Override
    public List<ClusteringResult> createClustering(List<ClusteringRequest> clusteringRequestList) {
        if(clusteringRequestList.size() < 2){
            throw CustomIllegalArgumentException.message("현재 좌표는 2개 이상이어야합니다.");
        }
        List<GeoPoint> geoPointList =  new ArrayList<>();
        Map<GeoPoint, RDTO> geoPointLocationMap = new HashMap<>();
        for(ClusteringRequest clusteringRequest : clusteringRequestList){
            clusteringRequest.validCheck(); //유효값 검사
            GeoPoint geoPoint = GeoPoint.of(clusteringRequest.getLat(), clusteringRequest.getLon());
            geoPointList.add(geoPoint);
            geoPointLocationMap.put(geoPoint, clusteringRequest.getLocationInfo());
        }
        double[][] geoPointArray = getGeoPointArray(geoPointList);

        int groupSize = clusteringRequestList.size()/4;
        KMeans clusters = PartitionClustering.run(20, () -> KMeans.fit(geoPointArray, groupSize < 2 ? 2 : groupSize));
        //kmeans 최소개수를 2개 이하로 할 때 에러가 나기 때문.

        Map<Integer, List<GeoPoint>> groupIdGeoPointMap = new HashMap<>();
        for (int i = 0, yLength = clusters.y.length; i < yLength; i++) {
            int groupId = clusters.y[i];
            GeoPoint geoPoint = geoPointList.get(i);
            if(geoPoint != null){
                groupIdGeoPointMap.computeIfAbsent(groupId, k -> new ArrayList()).add(geoPoint);
            }
        }

        List<ClusteringResult> clusteringResultList = new ArrayList<>();
        for(Map.Entry<Integer, List<GeoPoint>> entry : groupIdGeoPointMap.entrySet()){
            clusteringResultList.add(ClusteringResult.of(entry.getKey(), entry.getValue(), geoPointLocationMap));
        }
        return clusteringResultList;
    }


    private double[][] getGeoPointArray(List<GeoPoint> geoPointList) {
        if (CollectionUtils.isEmpty(geoPointList)) {
            return new double[0][];
        }
        double[][] geoPointArray = new double[geoPointList.size()][];
        int index = 0;
        for (GeoPoint geoPoint : geoPointList) {
            geoPointArray[index++] = new double[]{geoPoint.getLat(), geoPoint.getLon()};
        }
        return geoPointArray;
    }
}