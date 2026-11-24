package com.example.spring.core.notification;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class NotificationService {
    private final Map<String, Notifier> notifiers;

    public NotificationService(List<Notifier> notifierList) {
        this.notifiers = notifierList.stream()
                .collect(Collectors.toMap(Notifier::channel, n -> n));
    }

    public void send(String channel, String to, String message) {
        Notifier notifier = notifiers.get(channel);
        if (notifier == null) {
            throw new IllegalArgumentException("Unknown channel: " + channel);
        }
        notifier.send(to, message);
    }

    public void sendToAll(String to, String message) {
        notifiers.values().forEach(n -> n.send(to, message));
    }
}