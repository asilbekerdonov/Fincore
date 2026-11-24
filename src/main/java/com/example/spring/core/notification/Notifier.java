package com.example.spring.core.notification;

public interface Notifier {
    String channel();              // "email", "sms", "telegram"
    void send(String to, String message);
}