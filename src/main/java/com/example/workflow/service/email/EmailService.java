package com.example.workflow.service.email;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private EmailStrategy emailStrategy;

    public void setEmailStrategy(EmailStrategy emailStrategy) {
        this.emailStrategy = emailStrategy;
    }

    public void sendEmail(String recipient, String message) {
        emailStrategy.sendEmail(recipient, message);
    }
}
