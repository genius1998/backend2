package com.example.demo2.repository;

import com.example.demo2.entity.Marker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarkerRepository extends JpaRepository<Marker, Long> {
    List<Marker> findByPolylineId(Long polylineId);
}
