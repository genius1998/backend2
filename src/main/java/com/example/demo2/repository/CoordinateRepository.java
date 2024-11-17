package com.example.demo2.repository;

import com.example.demo2.entity.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    List<Coordinate> findByUserId(String userId); // userId로 검색
    List<Coordinate> findByPolylineId(Long polylineId); // 특정 Polyline ID로 좌표 조회
}
