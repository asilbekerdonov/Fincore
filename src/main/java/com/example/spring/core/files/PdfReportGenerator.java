package com.example.spring.core.files;

import org.springframework.stereotype.Service;

@Service
public class PdfReportGenerator implements ReportGenerator{
    @Override
    public String type() {
        return "pdf";
    }

    @Override
    public String generate(String data) {
        return "Pdf: " + data ;
    }
}
