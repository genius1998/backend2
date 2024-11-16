package com.example.demo2.repository;

import com.example.demo2.entity.TravelRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRecordRepository extends JpaRepository<TravelRecord, Long> {
}
