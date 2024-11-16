package com.example.demo2.controller;

import com.example.demo2.entity.User;
import com.example.demo2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/edit")
@CrossOrigin(origins = "http://localhost:3000") // 특정 도메인 허용
public class EditInfoController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/EditInfo")
    public ResponseEntity<Map<String, Object>> getUserById(@RequestParam String userId) {
        System.out.println("11111111111");
        System.out.println("userId: " + userId);

        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            Map<String, Object> response = new HashMap<>();
            response.put("code", 0); // 성공 코드
            response.put("message", "User found");
            response.put("data", Map.of(
                    "userId", user.getUserId(),
                    "password", user.getPassword()
            ));
            System.out.println("response: " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1); // 실패 코드
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/realedit")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId2"); // userId2로 변경
        String password = requestBody.get("userPw"); // userPw로 변경

        System.out.println("Updating user with ID: " + userId);
        System.out.println("New password: " + password);

        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Update user details
            user.setUserPw(password); // 비밀번호 업데이트
            userRepository.save(user); // 변경 사항 저장

            Map<String, Object> response = new HashMap<>();
            response.put("code", 0);
            response.put("message", "User updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // 회원탈퇴 API
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestBody Map<String, Object> requestBody) {
        String userId = (String) requestBody.get("user");

        System.out.println("Deleting user with ID: " + userId);

        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get()); // 사용자 삭제

            Map<String, Object> response = new HashMap<>();
            response.put("code", 0);
            response.put("message", "User deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1);
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
