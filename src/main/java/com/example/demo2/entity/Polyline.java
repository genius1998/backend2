package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Polyline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @OneToMany(mappedBy = "polyline", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coordinate> coordinates; // 좌표 리스트
}
