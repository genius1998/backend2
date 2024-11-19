package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Marker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @Column(nullable = false)
    private double lat; // 위도

    @Column(nullable = false)
    private double lng; // 경도

    @Column(nullable = true, length = 500)
    private String memo; // 메모 내용

    @ManyToOne
    @JoinColumn(name = "polyline_id", nullable = false)
    private Polyline polyline; // Polyline과 연관 관계
}
