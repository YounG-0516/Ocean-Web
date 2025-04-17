package com.example.waterquality.service;

import com.example.waterquality.model.WaterQualityData;
import com.example.waterquality.repository.WaterQualityRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WaterQualityService {
    
    private final WaterQualityRepository repository;
    private List<WaterQualityData> dataList = new ArrayList<>();
    private final String dataFilePath = "E:/Research/Undergraduate/GraduationProject_major/web/ocean/data/ETTh1.csv";
    
    @Autowired
    public WaterQualityService(WaterQualityRepository repository) {
        this.repository = repository;
        loadDataFromFile();
    }
    
    public List<WaterQualityData> getAllData() {
        return dataList;
    }
    
    private void loadDataFromFile() {
        try {
            Path path = Paths.get(dataFilePath);
            if (Files.exists(path)) {
                try (CSVReader reader = new CSVReader(new FileReader(dataFilePath))) {
                    String[] line;
                    // 跳过标题行
                    reader.readNext();
                    
                    while ((line = reader.readNext()) != null) {
                        WaterQualityData data = new WaterQualityData();
                        data.setDate(LocalDateTime.parse(line[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        data.setHUFL(Double.parseDouble(line[1]));
                        data.setHULL(Double.parseDouble(line[2]));
                        data.setMUFL(Double.parseDouble(line[3]));
                        data.setMULL(Double.parseDouble(line[4]));
                        data.setLUFL(Double.parseDouble(line[5]));
                        data.setLULL(Double.parseDouble(line[6]));
                        data.setOT(Double.parseDouble(line[7]));
                        
                        dataList.add(data);
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
    
    public void updateDataFile(MultipartFile file) throws IOException, CsvValidationException {
        // 保存上传的文件
        File dataFile = new File(dataFilePath);
        file.transferTo(dataFile);
        
        // 重新加载数据
        dataList.clear();
        loadDataFromFile();
    }
    
    public void importCSV(MultipartFile file) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            // 跳过标题行
            reader.readNext();
            
            while ((line = reader.readNext()) != null) {
                WaterQualityData data = new WaterQualityData();
                data.setDate(LocalDateTime.parse(line[0], DateTimeFormatter.ISO_DATE_TIME));
                data.setTemperature(Double.parseDouble(line[1]));
                data.setSalinity(Double.parseDouble(line[2]));
                data.setDissolvedOxygen(Double.parseDouble(line[3]));
                data.setPH(Double.parseDouble(line[4]));
                data.setTurbidity(Double.parseDouble(line[5]));
                data.setChlorophyll(Double.parseDouble(line[6]));
                data.setNitrate(Double.parseDouble(line[7]));
                data.setPhosphate(Double.parseDouble(line[8]));
                data.setSilicate(Double.parseDouble(line[9]));
                
                repository.save(data);
            }
        }
    }
} 