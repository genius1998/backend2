package com.example.demo2.controller;

import com.example.demo2.dto.UserLoginRequest;
import com.example.demo2.entity.TravelRecord;
import com.example.demo2.entity.User;
import com.example.demo2.repository.TravelRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // 특정 도메인 허용
public class ListController {

    @Autowired
    private TravelRecordRepository travelRecordRepository;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllTravelRecords() {
        List<TravelRecord> travelRecords = travelRecordRepository.findAll();

        // 결과를 Map에 담아 반환
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0); // 성공 코드
        response.put("message", "Travel records fetched successfully");
        response.put("data", travelRecords);
        System.out.println("response" + response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/travel-records")
    public ResponseEntity<Map<String, Object>> createTravelRecord(@RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("title");
        String description = requestBody.get("description");

        // 새로운 여행 기록 생성
        TravelRecord newRecord = new TravelRecord();
        newRecord.setTitle(title);
        newRecord.setDescription(description);
        newRecord.setCreatedDate(LocalDateTime.now()); // 현재 시간으로 생성일 설정

        // 데이터 저장
        travelRecordRepository.save(newRecord);

        // 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0); // 성공 코드
        response.put("message", "Travel record created successfully");
        response.put("data", newRecord);

        System.out.println("New travel record created: " + newRecord);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
