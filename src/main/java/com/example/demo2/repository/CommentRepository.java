package com.example.demo2.repository;

import com.example.demo2.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 TravelRecord에 연결된 댓글을 가져오는 메서드
    List<Comment> findByTravelRecordId(Long travelRecordId);
}
