package com.example.demo2.controller;

import com.example.demo2.entity.TravelRecord;
import com.example.demo2.repository.TravelRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/detail3")
@CrossOrigin(origins = "http://localhost:3000") // 특정 도메인 허용
public class DetailListController {

    @Autowired
    private TravelRecordRepository travelRecordRepository;

    // 특정 여행 기록 가져오기
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTravelRecordById(@PathVariable Long id) {
        // ID로 TravelRecord 검색
        Optional<TravelRecord> optionalTravelRecord = travelRecordRepository.findById(id);

        if (optionalTravelRecord.isPresent()) {
            TravelRecord travelRecord = optionalTravelRecord.get();

            // 결과를 Map에 담아 반환
            Map<String, Object> response = new HashMap<>();
            response.put("code", 0); // 성공 코드
            response.put("message", "Travel record fetched successfully");
            response.put("data", travelRecord);

            System.out.println("Fetched travel record: " + travelRecord);

            return ResponseEntity.ok(response);
        } else {
            // ID에 해당하는 데이터가 없을 경우
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1); // 실패 코드
            response.put("message", "Travel record not found");

            System.err.println("Travel record not found for ID: " + id);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
