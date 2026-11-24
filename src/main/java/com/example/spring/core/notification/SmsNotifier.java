package com.example.spring.core.notification;



import org.springframework.stereotype.Service;


@Service

public class SmsNotifier implements Notifier{
    @Override
    public String channel() {
        return "sms";
    }

    @Override
    public void send(String to, String message) {
        System.out.println("Send by sms!");
    }
}
