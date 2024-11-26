package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "polyline_id", nullable = false)
    @ToString.Exclude // 순환 참조 방지
    private Polyline polyline;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = true) // userId는 null 가능하도록 설정 (필요에 따라 변경)
    private String userId; // 사용자 ID
}
