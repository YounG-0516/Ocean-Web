package com.example.waterquality.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "water_quality_data")
public class WaterQualityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date")
    private LocalDateTime date;
    
    @Column(name = "hufl")
    private Double HUFL;
    
    @Column(name = "hull")
    private Double HULL;
    
    @Column(name = "mufl")
    private Double MUFL;
    
    @Column(name = "mull")
    private Double MULL;
    
    @Column(name = "lufl")
    private Double LUFL;
    
    @Column(name = "lull")
    private Double LULL;
    
    @Column(name = "ot")
    private Double OT;
    
    @Column(name = "temperature")
    private Double temperature;
    
    @Column(name = "salinity")
    private Double salinity;
    
    @Column(name = "dissolved_oxygen")
    private Double dissolvedOxygen;
    
    @Column(name = "ph")
    private Double pH;
    
    @Column(name = "turbidity")
    private Double turbidity;
    
    @Column(name = "chlorophyll")
    private Double chlorophyll;
    
    @Column(name = "nitrate")
    private Double nitrate;
    
    @Column(name = "phosphate")
    private Double phosphate;
    
    @Column(name = "silicate")
    private Double silicate;
} 