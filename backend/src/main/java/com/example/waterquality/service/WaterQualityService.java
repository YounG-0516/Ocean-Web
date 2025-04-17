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
    private final String dataFilePath = "E:/Research/Undergraduate/GraduationProject_major/web/ocean/data/all.csv";
    
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
                        data.setDate(LocalDateTime.parse(line[0] + " 00:00:00", DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss")));
                        data.setChlorophyll(Double.parseDouble(line[1]));
                        data.setWaterTemperature(Double.parseDouble(line[2]));
                        data.setDissolvedOxygen(Double.parseDouble(line[3]));
                        data.setPH(Double.parseDouble(line[4]));
                        data.setSalinity(Double.parseDouble(line[5]));
                        data.setPressure(Double.parseDouble(line[6]));
                        data.setAirTemperature(Double.parseDouble(line[7]));
                        data.setRelativeHumidity(Double.parseDouble(line[8]));
                        
                        System.out.println("Loaded data - pH: " + data.getPH());
                        dataList.add(data);
                        repository.save(data);
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
} 