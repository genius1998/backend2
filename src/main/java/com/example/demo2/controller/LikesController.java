package com.example.demo2.controller;
import com.example.demo2.entity.Likes;
import com.example.demo2.service.LikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/like")
public class LikesController {

    @Autowired
    private LikesService likesService;

    @PostMapping("/{polylineId}")
    public ResponseEntity<Likes> updateLikes(
            @PathVariable String polylineId,
            @RequestBody Likes likesRequest
    ) {
        System.out.println("polylineId: " + polylineId);
        System.out.println("likesRequest: " + likesRequest);
        Likes updatedLikes = likesService.updateLikes(polylineId, likesRequest.getLikes());
        System.out.println("updatedLikes: " + updatedLikes);
        return ResponseEntity.ok(updatedLikes);
    }

    // 추가: 특정 polylineId의 likes 조회 엔드포인트
    @GetMapping("/{polylineId}")
    public ResponseEntity<Integer> getLikesByPolylineId(@PathVariable String polylineId) {
        int likes = likesService.getLikesByPolylineId(polylineId);
        System.out.println("likes: " + likes);
        return ResponseEntity.ok(likes);
    }
    // /all
    @GetMapping("/all")
    public ResponseEntity<List<Likes>> getAllLikes() {
        try {
            List<Likes> allLikes = likesService.getAllLikes(); // 서비스에서 모든 likes 데이터 가져오기
            System.out.println("allLikes: " + allLikes);
            return ResponseEntity.ok(allLikes); // 모든 likes 데이터 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 에러 시 500 응답
        }
    }

}

