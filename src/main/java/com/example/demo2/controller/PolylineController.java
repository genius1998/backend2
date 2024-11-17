package com.example.demo2.controller;

import com.example.demo2.entity.Coordinate;
import com.example.demo2.entity.Polyline;
import com.example.demo2.repository.CoordinateRepository;
import com.example.demo2.repository.PolylineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PolylineController {

    @Autowired
    private PolylineRepository polylineRepository;

    @PostMapping("/polyline")
    public String receivePolyline(@RequestBody Map<String, Object> requestBody) {
        try {
            // userId를 문자열로 처리
            String userId = (String) requestBody.get("userId");

            // 경로 데이터 가져오기
            List<Map<String, Double>> pathData = (List<Map<String, Double>>) requestBody.get("pathData");

            if (pathData == null || pathData.isEmpty()) {
                return "Polyline 데이터가 비어 있습니다.";
            }

            System.out.println("받은 Polyline 경로:");
            pathData.forEach(point -> System.out.println("lat: " + point.get("lat") + ", lng: " + point.get("lng")));
            System.out.println("User ID: " + userId);

            // Polyline 엔티티 생성 및 저장
            Polyline polyline = new Polyline();
            List<Coordinate> coordinates = new ArrayList<>();
            for (Map<String, Double> point : pathData) {
                Coordinate coordinate = new Coordinate();
                coordinate.setLat(point.get("lat"));
                coordinate.setLng(point.get("lng"));
                coordinate.setUserId(userId); // 문자열 userId 저장
                coordinate.setPolyline(polyline); // Polyline 참조 설정
                coordinates.add(coordinate);
            }
            polyline.setCoordinates(coordinates);

            // 데이터베이스 저장
            polylineRepository.save(polyline);

            return "Polyline 좌표와 User ID가 성공적으로 저장되었습니다.";
        } catch (Exception e) {
            return "데이터 처리 중 오류 발생: " + e.getMessage();
        }
    }

    @Autowired
    private CoordinateRepository coordinateRepository;
    // 사용자 ID로 여행 기록 조회
    @GetMapping("/travel/{userId}")
    public List<Map<String, Object>> getTravelRecordsByUserId(@PathVariable String userId) {
        // 데이터베이스에서 userId에 해당하는 모든 좌표를 조회
        List<Coordinate> coordinates = coordinateRepository.findByUserId(userId);

        // Polyline ID 별로 그룹화
        Map<Long, List<Coordinate>> groupedByPolylineId = coordinates.stream()
                .collect(Collectors.groupingBy(coord -> coord.getPolyline().getId()));

        // JSON 반환을 위한 데이터 포맷팅
        List<Map<String, Object>> result = new ArrayList<>();
        groupedByPolylineId.forEach((polylineId, coords) -> {
            Map<String, Object> polylineData = new HashMap<>();
            polylineData.put("polylineId", polylineId);
            polylineData.put("coordinates", coords.stream().map(coord -> {
                Map<String, Double> point = new HashMap<>();
                point.put("lat", coord.getLat());
                point.put("lng", coord.getLng());
                return point;
            }).collect(Collectors.toList()));
            result.add(polylineData);
        });

        return result;
    }

    // 특정 Polyline ID로 좌표 조회
    @GetMapping("/travel/detail/{polylineId}")
    public Map<String, Object> getCoordinatesByPolylineId(@PathVariable Long polylineId) {
        // Polyline ID에 해당하는 모든 좌표 조회
        List<Coordinate> coordinates = coordinateRepository.findByPolylineId(polylineId);

        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("해당 Polyline ID에 대한 데이터가 없습니다.");
        }

        // JSON 반환을 위한 데이터 포맷팅
        Map<String, Object> result = new HashMap<>();
        result.put("polylineId", polylineId);
        result.put("coordinates", coordinates.stream().map(coord -> {
            Map<String, Double> point = new HashMap<>();
            point.put("lat", coord.getLat());
            point.put("lng", coord.getLng());
            return point;
        }).collect(Collectors.toList()));
        System.out.println("result:"+result);
        return result;
    }

}
