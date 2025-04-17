package com.example.waterquality.controller;

import com.example.waterquality.model.WaterQualityData;
import com.example.waterquality.service.WaterQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/water-quality")
@CrossOrigin(origins = "*")
public class WaterQualityController {
    
    @Autowired
    private WaterQualityService service;
    
    @GetMapping
    public ResponseEntity<List<WaterQualityData>> getAllData() {
        return ResponseEntity.ok(service.getAllData());
    }
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        try {
            service.updateDataFile(file);
            return ResponseEntity.ok("CSV文件上传成功");
        } catch (IOException | com.opencsv.exceptions.CsvValidationException e) {
            return ResponseEntity.badRequest().body("文件上传失败: " + e.getMessage());
        }
    }
} 