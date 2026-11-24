package com.example.spring.core.notification;

import org.springframework.stereotype.Service;

@Service
public class TelegramNotifier  implements Notifier{
    @Override
    public String channel() {
        return "Telegram";
    }

    @Override
    public void send(String to, String message) {
        System.out.println("Send by telegram!");
    }
}
