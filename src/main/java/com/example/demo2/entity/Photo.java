package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @ManyToOne
    @JoinColumn(name = "polyline_id", nullable = false) // Polyline의 id와 연관
    private Polyline polyline; // Polyline 엔티티와 연결

    @Column(nullable = false)
    private String photoPath; // 사진 저장 경로 또는 URL 필드

    @Column
    private String description; // 사진 설명 필드 (선택)
}
