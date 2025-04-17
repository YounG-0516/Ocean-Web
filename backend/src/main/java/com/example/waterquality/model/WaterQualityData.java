package com.example.waterquality.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "water_quality_data")
@Getter
@Setter
public class WaterQualityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date")
    private LocalDateTime date;
    
    @Column(name = "chlorophyll")
    private Double chlorophyll;
    
    @Column(name = "water_temperature")
    private Double waterTemperature;
    
    @Column(name = "dissolved_oxygen")
    private Double dissolvedOxygen;
    
    @Column(name = "pH")
    private Double pH;
    
    @Column(name = "salinity")
    private Double salinity;
    
    @Column(name = "pressure")
    private Double pressure;
    
    @Column(name = "air_temperature")
    private Double airTemperature;
    
    @Column(name = "relative_humidity")
    private Double relativeHumidity;
} 