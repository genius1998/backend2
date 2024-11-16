package com.example.demo2.repository;

import com.example.demo2.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    // JpaRepository에 이미 save 메서드가 정의되어 있으므로 별도로 선언할 필요가 없습니다.
}
