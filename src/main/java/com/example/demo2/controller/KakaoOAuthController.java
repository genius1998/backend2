package com.example.demo2.controller;

import com.example.demo2.dto.UserLoginRequest;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserRepository;
import com.example.demo2.service.KakaoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

@RestController
public class KakaoOAuthController {

    private final KakaoService kakaoService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public KakaoOAuthController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @PostMapping("/login/success")
    public ResponseEntity<Map<String, Object>> loginSuccess(@RequestBody Map<String, Object> requestBody) {
        String accessToken = null;
        String refreshToken = null;
        Integer expires_in = null;
        Integer refresh_token_expires_in = null;
        UserLoginRequest loginRequest = new UserLoginRequest();
        try {
            Map<String, Object> response = (Map<String, Object>) requestBody.get("response");
            Map<String, Object> innerResponse = (Map<String, Object>) response.get("response");
            accessToken = (String) innerResponse.get("access_token");
            refreshToken = (String) innerResponse.get("refresh_token");
            expires_in = (Integer) innerResponse.get("expires_in");
            refresh_token_expires_in = (Integer) innerResponse.get("refresh_token_expires_in");
        } catch (Exception e) {
            System.out.println("Error extracting access_token: " + e.getMessage());
        }

        System.out.println("Access Token: " + accessToken);
        System.out.println("refresh Token: " + refreshToken);
        System.out.println("expires_in: " + expires_in);
        System.out.println("refresh_token_expires_in: " + refresh_token_expires_in);

        Map<String, Object> response = new HashMap<>();

        if (accessToken != null) {
            String userInfo = kakaoService.getUserInfo(accessToken, refreshToken, expires_in, refresh_token_expires_in);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode root = objectMapper.readTree(userInfo);
                Long userId = root.path("id").asLong();
                System.out.println("userId: " + userId);

                if (userRepository.findByUserId(userId.toString()).isPresent()) {
                    System.out.println("회원 존재");
                    response.put("code", 0);
                    response.put("message", "Login successful");
                    response.put("data", userId);
                    System.out.println("response!!:" + response);
                    return ResponseEntity.ok(response);
                } else {
                    response.put("code", 401);
                    response.put("message", "Login Failed: User not found.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            } catch (Exception e) {
                System.out.println("Error parsing user info: " + e.getMessage());
                response.put("code", 500);
                response.put("message", "Error processing user info.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("code", 400);
            response.put("message", "Access Token not found.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
