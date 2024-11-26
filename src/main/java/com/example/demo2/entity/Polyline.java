package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
public class Polyline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    private String userId; // 사용자 ID 필드 추가

    @OneToMany(mappedBy = "polyline", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // 순환 참조 방지
    private List<Coordinate> coordinates; // 좌표 리스트

    @OneToMany(mappedBy = "polyline", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // 순환 참조 방지
    private List<Marker> markers; // 마커 리스트

    @Column(nullable = false)
    private String title; // 제목 필드 추가

    @Column(nullable = false)
    private String visibility; // 공개 여부

    @Lob // 대용량 텍스트로 저장
    @Column(nullable = false)
    private String postContent; // 본문 내용 필드 추가

    @OneToMany(mappedBy = "polyline", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // 순환 참조 방지
    private List<Photo> photos; // 여러 사진 연결
}
