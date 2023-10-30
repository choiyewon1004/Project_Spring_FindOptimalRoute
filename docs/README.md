# 기능 1 _version1(join)
- 목적지를 중심으로 반경을 설정하여 반경 내의 [정류소에 정차하는 버스 / 역에 정차하는 지하철  ]목록(reachable_list)
- reachable_list에 존재하는 모든 [버스/지하철]이 운항하는 노선을 파악
- 파악된 노선 중 출발지와 목적지 사이에 존재하는 [정류장 / 역]을 목록화(final_list)

SELECT bsi.*
FROM busStationInfo AS bsi
JOIN busInfo AS bi ON bsi.busInfo_code = bi.busInfo_code
WHERE bi.busInfo_nm IN (
    SELECT busInfo_nm
    FROM busInfo
    WHERE busInfo_code IN (
        SELECT DISTINCT stopInfo_bus
        FROM stopInfo
        WHERE stopInfo_station IN (
            SELECT stationInfo_code
            FROM stationInfo
            WHERE ST_Distance_Sphere(@location, POINT(stationInfo_lng, stationInfo_lat)) <= 500
        )
    )
);

### 기능 1-1 : 반경 내 정류소 출력
SELECT stationInfo_code
FROM stationInfo
WHERE ST_Distance_Sphere(@location, POINT(stationInfo_lng, stationInfo_lat)) <= 500

### 기능 1-2 : 반경 내 정류소에 정차하는 버스 목록
SELECT DISTINCT stopInfo_bus
FROM stopInfo
WHERE stopInfo_station IN ( 기능 1-1 )

### 기능 1-3 : 반경 내 정류소에 정차하는 버스 번호
SELECT busInfo_nm
FROM busInfo
WHERE busInfo_code IN ( 기능 1-2 ) 

### 기능 1-4 : 최종 위경도 출력
SELECT *
FROM busStationInfo AS bsi
JOIN busInfo AS bi ON bsi.busInfo_code = bi.busInfo_code
WHERE bi.busInfo_nm IN ( 기능 1-3 ) 

# 기능 1 _version2(sub query)
SELECT *
FROM testdb.stationinfo
WHERE
    (stationInfo_code IN(
        SELECT DISTINCT stopInfo_station
        FROM testdb.stopinfo
        WHERE stopInfo_bus IN (
            SELECT stopInfo_bus
            FROM testdb.stopinfo
            WHERE stopinfo_station IN (
                SELECT stationInfo_code
                FROM testdb.stationinfo
                WHERE ST_Distance_Sphere(@location, POINT(stationInfo_lng, stationInfo_lat)) <= @set_radius
                )
            )
        )
    )
    AND
    stationinfo_lng BETWEEN @small_lng AND @big_lng
    AND
    stationinfo_lat BETWEEN @small_lat AND @big_lat
);



### 기능 1-1 : 목적지 근방 버스 정류장 검색
select *
from testdb.stationinfo
where ST_Distance_Sphere(POINT(@end_lng, @end_lat), POINT(stationInfo_lng, stationInfo_lat)) <= @set_radius;

### 기능 1-2 : 해당 정류장 멈추는 버스 번호 확인
SELECT *
FROM testdb.stopinfo
WHERE stopinfo_station IN (기능 1-1 )

### 기능 1-3 : 멈추는 버스가 지나치는 노선 확인
SELECT count(*)
FROM testdb.stopinfo
WHERE stopInfo_bus IN (기능 1-2);


### 기능 1-4 : 해당 정류장의 위경도 출력
SELECT *
FROM testdb.stationinfo
WHERE
    (기능 1-3)
    and
    (stationinfo_lng BETWEEN @small_lng AND @big_lng)
    AND
    (stationinfo_lat BETWEEN @small_lat AND @big_lat)
)

# 기능 2
- final_list의 위경도 값을 가져와 군집화 
- java를 이용한 군집화 구현
- 최종 결과값 List<ClusteringResult>

# 기능 3
- API를 통해 출발점부터 군집 중심들까지의 택시 거리와 비용을 계산


# 기능 4
- 가장 짧은 군집을 찾은 후 해당 군집에서 최적의 [정류장 / 역]을 찾기

# 기능 5
- 최종적으로 출발지~[군집에서 찾은 최적의 위치]~목적지 로 경로가 구성
