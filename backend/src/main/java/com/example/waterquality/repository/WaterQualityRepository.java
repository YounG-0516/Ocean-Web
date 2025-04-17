package com.example.waterquality.repository;

import com.example.waterquality.model.WaterQualityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaterQualityRepository extends JpaRepository<WaterQualityData, Long> {
} 