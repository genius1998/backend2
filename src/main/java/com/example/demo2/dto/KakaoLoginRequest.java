package com.example.demo2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoLoginRequest {

    private Response response;
    private Profile profile;

    @Data
    public static class Response {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("id_token")
        private String idToken;

        @JsonProperty("expires_in")
        private int expiresIn;

        private String scope;

        @JsonProperty("refresh_token_expires_in")
        private int refreshTokenExpiresIn;
    }

    @Data
    public static class Profile {
        private long id;

        @JsonProperty("connected_at")
        private String connectedAt;

        private Properties properties;

        @JsonProperty("kakao_account")
        private KakaoAccount kakaoAccount;
    }

    @Data
    public static class Properties {
        private String nickname;

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("thumbnail_image")
        private String thumbnailImage;
    }

    @Data
    public static class KakaoAccount {
        @JsonProperty("profile_nickname_needs_agreement")
        private boolean profileNicknameNeedsAgreement;

        @JsonProperty("profile_image_needs_agreement")
        private boolean profileImageNeedsAgreement;

        private ProfileDetails profile;
    }

    @Data
    public static class ProfileDetails {
        private String nickname;

        @JsonProperty("thumbnail_image_url")
        private String thumbnailImageUrl;

        @JsonProperty("profile_image_url")
        private String profileImageUrl;

        @JsonProperty("is_default_image")
        private boolean isDefaultImage;

        @JsonProperty("is_default_nickname")
        private boolean isDefaultNickname;
    }
}
