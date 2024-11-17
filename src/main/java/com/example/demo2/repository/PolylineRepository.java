package com.example.demo2.repository;

import com.example.demo2.entity.Polyline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolylineRepository extends JpaRepository<Polyline, Long> {
}
