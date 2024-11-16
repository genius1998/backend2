// src/main/java/com/example/demo/controller/UserController.java
package com.example.demo2.controller;

import com.example.demo2.dto.UserLoginRequest;
import com.example.demo2.dto.UserRegisterRequest;
import com.example.demo2.entity.User;
import com.example.demo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000") // 특정 도메인 허용
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // 로그인 엔드포인트
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginRequest loginRequest) {
        System.out.println("Received User ID: " + loginRequest.getUserId());
        System.out.println("Received Password: " + loginRequest.getUserPw());

        User user = userRepository.findByUserId(loginRequest.getUserId()).orElse(null);
        System.out.println("1");
        Map<String, Object> response = new HashMap<>();
        System.out.println("2");
        if (user == null) {
            response.put("code", 1);
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        System.out.println("3");
        if (!user.getPassword().equals(loginRequest.getUserPw())) {
            response.put("code", 2);
            response.put("message", "Invalid password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        System.out.println("여기까지 온것은 user가 조회되고 비밀번호도 일치함을 의미..");
        response.put("code", 0);
        response.put("message", "Login successful");
        response.put("data", user);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserRegisterRequest registerRequest){
        System.out.println("Received User ID: " + registerRequest.getUserId());
        System.out.println("Received Password: " + registerRequest.getUserPw());

        Map<String, Object> response = new HashMap<>();

        // Check if user ID already exists
        if (userRepository.findByUserId(registerRequest.getUserId()).isPresent()) {
            response.put("code", 1);
            response.put("message", "이미 존재하는 아이디 입니다!!");
            System.out.println("이미 존재하는 아이디 입니다!!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Create and save new user
        User newUser = new User();
        newUser.setUserId(registerRequest.getUserId());
        newUser.setUserPw(registerRequest.getUserPw());
        userRepository.save(newUser);

        response.put("code", 0);
        response.put("message", "Registration successful");
        response.put("data", null);
        System.out.println("정상적으로 등록됨");
        return ResponseEntity.ok(response);
    }


}

