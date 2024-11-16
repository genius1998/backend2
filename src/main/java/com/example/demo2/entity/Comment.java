package com.example.demo2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment") // 테이블 이름 명시
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "travel_record_id", nullable = false) // 외래 키 명시
    @JsonBackReference // 순환 참조 방지: 자식 -> 부모 방향
    private TravelRecord travelRecord;

    private String content;
    private String author;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TravelRecord getTravelRecord() {
        return travelRecord;
    }

    public void setTravelRecord(TravelRecord travelRecord) {
        this.travelRecord = travelRecord;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
