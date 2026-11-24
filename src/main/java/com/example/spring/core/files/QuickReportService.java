package com.example.spring.core.files;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class QuickReportService {
    private final ReportGenerator defaultGenerator;   // CSV через @Primary
    private final ReportGenerator pdfGenerator;       // PDF через @Qualifier

    public QuickReportService(ReportGenerator defaultGenerator,
                              @Qualifier("pdfReportGenerator") ReportGenerator pdfGenerator) {
        this.defaultGenerator = defaultGenerator;
        this.pdfGenerator = pdfGenerator;
    }

    public String report(String data) {
        return defaultGenerator.generate(data) + " | " + pdfGenerator.generate(data);
    }
}
