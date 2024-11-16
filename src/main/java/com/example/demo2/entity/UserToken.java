package com.example.demo2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키 (Primary Key)

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
    private User user; // User 엔티티와의 관계

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "access_token_expiration")
    private Timestamp accessTokenExpiration;

    @Column(name = "refresh_token_expiration")
    private Timestamp refreshTokenExpiration;

    public void updateTokens(String accessToken, String refreshToken, Timestamp accessTokenExpiration, Timestamp refreshTokenExpiration) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public boolean isAboutToExpire() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        long timeDifference = accessTokenExpiration.getTime() - now.getTime();
        return timeDifference <= (10 * 60 * 1000); // 10분 (밀리초 기준)
    }
}
