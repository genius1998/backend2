package com.example.demo2.controller;

import com.example.demo2.entity.Coordinate;
import com.example.demo2.entity.Marker;
import com.example.demo2.entity.Polyline;
import com.example.demo2.repository.CoordinateRepository;
import com.example.demo2.repository.MarkerRepository;
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

    @Autowired
    private MarkerRepository markerRepository;

    @PostMapping("/polyline")
    public String receivePolyline(@RequestBody Map<String, Object> requestBody) {
        try {
            String userId = (String) requestBody.get("userId");
            String title = (String) requestBody.get("title");
            List<Map<String, Double>> pathData = (List<Map<String, Double>>) requestBody.get("pathData");
            List<Map<String, Object>> markersData = (List<Map<String, Object>>) requestBody.get("markersData");

            if (pathData == null || pathData.isEmpty()) {
                return "Polyline 데이터가 비어 있습니다.";
            }

            // Polyline 생성 및 저장
            Polyline polyline = new Polyline();
            polyline.setTitle(title);

            List<Coordinate> coordinates = new ArrayList<>();
            for (Map<String, Double> point : pathData) {
                Coordinate coordinate = new Coordinate();
                coordinate.setLat(point.get("lat"));
                coordinate.setLng(point.get("lng"));
                coordinate.setUserId(userId);
                coordinate.setPolyline(polyline);
                coordinates.add(coordinate);
            }
            polyline.setCoordinates(coordinates);
            polylineRepository.save(polyline);

            // Marker 생성 및 저장
            if (markersData != null) {
                for (Map<String, Object> markerData : markersData) {
                    Marker marker = new Marker();
                    marker.setLat((Double) markerData.get("lat"));
                    marker.setLng((Double) markerData.get("lng"));
                    marker.setMemo((String) markerData.get("memo"));
                    marker.setPolyline(polyline);
                    markerRepository.save(marker);
                }
            }

            return "Polyline과 마커 데이터가 성공적으로 저장되었습니다.";
        } catch (Exception e) {
            return "데이터 처리 중 오류 발생: " + e.getMessage();
        }
    }

    @Autowired
    private CoordinateRepository coordinateRepository;
    // 사용자 ID로 여행 기록 조회
    @GetMapping("/travel/{userId}")
    public List<Map<String, Object>> getTravelRecordsByUserId(@PathVariable String userId) {
        List<Coordinate> coordinates = coordinateRepository.findByUserId(userId);

        Map<Long, List<Coordinate>> groupedByPolylineId = coordinates.stream()
                .collect(Collectors.groupingBy(coord -> coord.getPolyline().getId()));

        List<Map<String, Object>> result = new ArrayList<>();
        groupedByPolylineId.forEach((polylineId, coords) -> {
            Map<String, Object> polylineData = new HashMap<>();
            polylineData.put("polylineId", polylineId);
            polylineData.put("title", coords.get(0).getPolyline().getTitle()); // 제목 추가
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


    @GetMapping("/travel/all")
    public List<Map<String, Object>> getAllTravelRecords() {
        // 데이터베이스에서 모든 좌표를 조회
        List<Coordinate> coordinates = coordinateRepository.findAll();

        // Polyline ID 별로 그룹화
        Map<Long, List<Coordinate>> groupedByPolylineId = coordinates.stream()
                .collect(Collectors.groupingBy(coord -> coord.getPolyline().getId()));

        // JSON 반환을 위한 데이터 포맷팅
        List<Map<String, Object>> result = new ArrayList<>();
        groupedByPolylineId.forEach((polylineId, coords) -> {
            Map<String, Object> polylineData = new HashMap<>();
            polylineData.put("polylineId", polylineId);

            // Polyline ID로 Polyline 조회하여 제목 가져오기
            Polyline polyline = polylineRepository.findById(polylineId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 Polyline ID에 대한 데이터가 없습니다."));

            polylineData.put("title", polyline.getTitle()); // 제목 추가
            polylineData.put("coordinates", coords.stream().map(coord -> {
                Map<String, Double> point = new HashMap<>();
                point.put("lat", coord.getLat());
                point.put("lng", coord.getLng());
                return point;
            }).collect(Collectors.toList()));
            result.add(polylineData);
        });

        System.out.println("result(all): " + result);

        return result;
    }



    // 특정 Polyline ID로 좌표 및 제목 조회
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
        result.put("title", coordinates.get(0).getPolyline().getTitle()); // 제목 추가
        result.put("coordinates", coordinates.stream().map(coord -> {
            Map<String, Double> point = new HashMap<>();
            point.put("lat", coord.getLat());
            point.put("lng", coord.getLng());
            return point;
        }).collect(Collectors.toList()));

        System.out.println("result with title:"+result);
        return result;
    }

    // 특정 Polyline ID로 좌표 삭제
    @DeleteMapping("/travel/{polylineId}")
    public String deleteTravelRecordByPolylineId(@PathVariable Long polylineId) {
        try {
            // Polyline과 관련된 모든 Coordinate 삭제
            List<Coordinate> coordinates = coordinateRepository.findByPolylineId(polylineId);
            coordinateRepository.deleteAll(coordinates);

            // Polyline 자체 삭제
            polylineRepository.deleteById(polylineId);

            return "Polyline ID " + polylineId + "와 관련된 여행 기록이 삭제되었습니다.";
        } catch (Exception e) {
            return "삭제 중 오류 발생: " + e.getMessage();
        }
    }

    @GetMapping("/polyline/{polylineId}/markers")
    public List<Map<String, Object>> getMarkersByPolylineId(@PathVariable Long polylineId) {
        List<Marker> markers = markerRepository.findByPolylineId(polylineId);

        return markers.stream().map(marker -> {
            Map<String, Object> markerData = new HashMap<>();
            markerData.put("id", marker.getId());
            markerData.put("lat", marker.getLat());
            markerData.put("lng", marker.getLng());
            markerData.put("memo", marker.getMemo());
            return markerData;
        }).collect(Collectors.toList());
    }
}
