package com.example.spring.core.files;

public interface ReportGenerator {
    String type();
    String generate(String data);

}
