package com.example.spring.core.files;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class CsvReportGenerator implements ReportGenerator{
    @Override
    public String type() {
        return "csv";
    }

    @Override
    public String generate(String data) {
        return "csv: " + data;
    }
}
