package com.example.demo2.repository;
import com.example.demo2.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Likes findByPolylineId(String polylineId);
}

