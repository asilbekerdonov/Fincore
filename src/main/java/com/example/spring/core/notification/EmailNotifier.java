package com.example.spring.core.notification;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class EmailNotifier implements Notifier{
    @Override
    public String channel() {
        return "email";
    }

    @Override
    public void send(String to, String message) {
        System.out.println("Email " + to + message);
    }
}
