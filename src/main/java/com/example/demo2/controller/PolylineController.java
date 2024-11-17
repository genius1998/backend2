package com.example.demo2.controller;

import com.example.demo2.entity.Coordinate;
import com.example.demo2.entity.Polyline;
import com.example.demo2.repository.PolylineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PolylineController {

    @Autowired
    private PolylineRepository polylineRepository;

    @PostMapping("/polyline")
    public String receivePolyline(@RequestBody List<Map<String, Double>> pathData) {
        if (pathData == null || pathData.isEmpty()) {
            return "Polyline 데이터가 비어 있습니다.";
        }

        System.out.println("받은 Polyline 경로:");
        pathData.forEach(point -> System.out.println("lat: " + point.get("lat") + ", lng: " + point.get("lng")));

        // Polyline 엔티티 생성 및 저장
        Polyline polyline = new Polyline();
        List<Coordinate> coordinates = new ArrayList<>();
        for (Map<String, Double> point : pathData) {
            Coordinate coordinate = new Coordinate();
            coordinate.setLat(point.get("lat"));
            coordinate.setLng(point.get("lng"));
            coordinate.setPolyline(polyline); // Polyline 참조 설정
            coordinates.add(coordinate);
        }
        polyline.setCoordinates(coordinates);

        // 데이터베이스 저장
        polylineRepository.save(polyline);

        return "Polyline 좌표가 성공적으로 저장되었습니다.";
    }
}
