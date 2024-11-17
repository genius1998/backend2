package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    private Double lat; // 위도
    private Double lng; // 경도

    @ManyToOne
    @JoinColumn(name = "polyline_id", nullable = false)
    private Polyline polyline; // Polyline 참조
}
