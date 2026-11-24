package com.example.spring.core.notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    private final Notifier EmailNotifier;   // придёт EmailNotifier (@Primary)
    private final Notifier smsNotifier;       // придёт SmsNotifier (@Qualifier)

    public AlertService(Notifier defaultNotifier,
                        @Qualifier("smsNotifier") Notifier smsNotifier) {
        this.EmailNotifier = defaultNotifier;
        this.smsNotifier = smsNotifier;
    }

    public void alert(String to, String msg) {
        EmailNotifier.send(to, msg);
        smsNotifier.send(to, msg);
    }
}