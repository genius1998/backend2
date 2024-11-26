package com.example.demo2.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String polylineId;

    private int likes;

    public Likes() {
    }

    public Likes(String polylineId, int likes) {
        this.polylineId = polylineId;
        this.likes = likes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolylineId() {
        return polylineId;
    }

    public void setPolylineId(String polylineId) {
        this.polylineId = polylineId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}

