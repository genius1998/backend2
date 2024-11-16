// src/main/java/com/example/demo/dto/UserLoginRequest.java
package com.example.demo2.dto;

public class UserRegisterRequest {
    private String userId;
    private String userPw;

    // 기본 생성자
    public UserRegisterRequest() {}

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for userPw
    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }


}
