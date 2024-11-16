package com.example.demo2.service;

import com.example.demo2.entity.User;
import com.example.demo2.entity.UserToken;
import com.example.demo2.repository.UserTokenRepository;
import com.example.demo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class KakaoService {

    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public KakaoService(UserTokenRepository userTokenRepository, UserRepository userRepository) {
        this.userTokenRepository = userTokenRepository;
        this.userRepository = userRepository;
    }

    public String getUserInfo(String accessToken, String refreshToken, Integer expire, Integer refreshExpire) {
        String url = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        // 헤더에 Authorization 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println("response.getBody(): " + response.getBody());

        // JSON 응답 파싱
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            String nickname = root.path("kakao_account").path("profile").path("nickname").asText();
            Long userId = root.path("id").asLong();

            User user = userRepository.findByUserId(userId.toString())
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setUserId2(userId);
                        //newUser.setName(nickname);
                        newUser.setUserPw("default_password");  // 기본 비밀번호 설정
                        return userRepository.save(newUser);  // 잘못된 부분 수정
                    });

            // 만료 시간 설정
            long expirationTimeMillis = System.currentTimeMillis() + (expire.longValue() * 1000L);
            long refreshExpirationTimeMillis = System.currentTimeMillis() + (refreshExpire.longValue() * 1000L);

            // Timestamp 변환
            Timestamp accessTokenExpiration2 = new Timestamp(expirationTimeMillis);
            Timestamp refreshTokenExpiration2 = new Timestamp(refreshExpirationTimeMillis);

            // 결과 출력
            System.out.println("Access Token Expiration: " + accessTokenExpiration2);
            System.out.println("Refresh Token Expiration: " + refreshTokenExpiration2);

            // UserToken 엔티티 생성 및 저장
            UserToken userToken = UserToken.builder()
                    .user(user)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .accessTokenExpiration(accessTokenExpiration2)
                    .refreshTokenExpiration(refreshTokenExpiration2)
                    .name(nickname)
                    .build();

            userTokenRepository.save(userToken);

        } catch (Exception e) {
            System.out.println("Error parsing JSON or saving to database: " + e.getMessage());
        }

        return response.getBody();
    }

    public void refreshAccessToken(UserToken userToken) {
        System.out.println("refreshAccessToken 실행");
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", "1dd6d1136d01945f0fbf40c72205d7bb");
        params.add("refresh_token", userToken.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("refreshAccessToken 실행2");

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());

            String newAccessToken = root.path("access_token").asText();
            int expiresIn = root.path("expires_in").asInt();
            Timestamp newExpiration = new Timestamp(System.currentTimeMillis() + expiresIn * 1000L);

            userToken.setAccessToken(newAccessToken);
            userToken.setAccessTokenExpiration(newExpiration);
            userTokenRepository.save(userToken);
            System.out.println("refreshAccessToken 실행3");

        } catch (Exception e) {
            System.out.println("Error refreshing token: " + e.getMessage());
        }
        System.out.println("refreshAccessToken 실행4");
    }
}
