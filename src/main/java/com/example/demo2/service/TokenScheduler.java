package com.example.demo2.service;

import com.example.demo2.entity.UserToken;
import com.example.demo2.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenScheduler {

    private final KakaoService kakaoService;
    private final UserTokenRepository userTokenRepository;

    @Autowired
    public TokenScheduler(KakaoService kakaoService, UserTokenRepository userTokenRepository) {
        this.kakaoService = kakaoService;
        this.userTokenRepository = userTokenRepository;
    }

    @Scheduled(fixedRate = 300000) // 5분마다 실행 (밀리초 기준) >> 지금은 테스트 위해 30초마다..
    public void refreshExpiringTokens() {
        System.out.println("30초 지남");
        Iterable<UserToken> tokens = userTokenRepository.findAll();
        for (UserToken token : tokens) {
            if (token.isAboutToExpire()) {
                kakaoService.refreshAccessToken(token);
            }
        }
        System.out.println("성공적으로 갱신됨!");
    }
}
