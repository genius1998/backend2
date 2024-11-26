package com.example.demo2.repository;

import com.example.demo2.entity.comment2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository2 extends JpaRepository<comment2, Long> {
    List<comment2> findByPolylineId(Long polylineId); // Polyline ID로 댓글 조회
}
