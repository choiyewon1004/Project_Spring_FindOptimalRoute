package com.example.project_spring_optimalroute.Cluster;

import com.example.project_spring_optimalroute.Route.RDTO;

import java.util.List;
import java.util.Map;

public interface KmeansClusteringService {

    List<ClusteringResult> getClusteringResult(Map<GeoPoint, RDTO> GEO_POINT_MAP);

    List<ClusteringResult> createClustering(List<ClusteringRequest> clusteringRequestList);
}