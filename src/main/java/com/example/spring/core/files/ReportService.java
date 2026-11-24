package com.example.spring.core.files;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final Map<String, ReportGenerator> reportGeneratorMap;

    public ReportService(List<ReportGenerator> reportGenerators) {
        this.reportGeneratorMap = reportGenerators.stream().collect(Collectors.toMap(ReportGenerator::type , n -> n));
    }

    public String create(String type, String data) {
        ReportGenerator reportGenerator = reportGeneratorMap.get(type);
        if(reportGenerator == null) {
            throw new IllegalArgumentException("Unknown type " + type);
        }

        return reportGenerator.generate(data);
    }
}
