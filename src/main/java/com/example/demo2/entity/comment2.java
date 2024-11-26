package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class comment2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글 ID

    @ManyToOne
    @JoinColumn(name = "polyline_id", nullable = false)
    private Polyline polyline; // Polyline과의 연관 관계

    @Column(nullable = false, length = 500)
    private String content; // 댓글 내용

    @Column(nullable = false)
    private String author; // 작성자

    @Column(nullable = false)
    private String timestamp; // 작성 시간
}
