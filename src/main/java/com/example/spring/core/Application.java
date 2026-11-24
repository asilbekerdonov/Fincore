package com.example.spring.core;

import com.example.spring.core.files.QuickReportService;
import com.example.spring.core.files.ReportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        
    }

}
