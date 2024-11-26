package com.example.demo2.service;
import com.example.demo2.entity.Likes;
import com.example.demo2.repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesService {

    @Autowired
    private LikesRepository likesRepository;

    public Likes updateLikes(String polylineId, int likes) {
        Likes existingLikes = likesRepository.findByPolylineId(polylineId);
        if (existingLikes != null) {
            existingLikes.setLikes(likes);
        } else {
            existingLikes = new Likes(polylineId, likes);
        }
        return likesRepository.save(existingLikes);
    }

    // 추가: 특정 polylineId에 해당하는 추천수 반환
    public int getLikesByPolylineId(String polylineId) {
        Likes likes = likesRepository.findByPolylineId(polylineId);
        return (likes != null) ? likes.getLikes() : 0; // likes가 없으면 0 반환
    }

    public List<Likes> getAllLikes() {
        return likesRepository.findAll(); // 모든 likes 데이터를 DB에서 가져옴
    }
}
