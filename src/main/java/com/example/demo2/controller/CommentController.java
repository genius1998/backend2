package com.example.demo2.controller;

import com.example.demo2.entity.Comment;
import com.example.demo2.entity.TravelRecord;
import com.example.demo2.repository.CommentRepository;
import com.example.demo2.repository.TravelRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TravelRecordRepository travelRecordRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addComment(@RequestBody Map<String, String> requestBody) {
        Long travelRecordId = Long.parseLong(requestBody.get("travelRecordId"));
        String content = requestBody.get("content");
        String author = requestBody.get("author");

        TravelRecord travelRecord = travelRecordRepository.findById(travelRecordId).orElse(null);

        if (travelRecord == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("message", "TravelRecord not found");
            return ResponseEntity.badRequest().body(response);
        }

        Comment comment = new Comment();
        comment.setTravelRecord(travelRecord);
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setCreatedDate(LocalDateTime.now());
        System.out.println("comment: " + comment);
        commentRepository.save(comment);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "Comment added successfully");
        return ResponseEntity.ok(response);
    }

    // 특정 TravelRecord에 대한 댓글 가져오기
    @GetMapping("/{travelRecordId}")
    public ResponseEntity<Map<String, Object>> getCommentsByTravelRecordId(@PathVariable Long travelRecordId) {
        System.out.println("여까지옴! ");
        List<Comment> comments = commentRepository.findByTravelRecordId(travelRecordId);

        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "댓글 목록 가져오기 성공");
        response.put("data", comments);
        System.out.println("response: " + response);
        return ResponseEntity.ok(response);
    }
}
