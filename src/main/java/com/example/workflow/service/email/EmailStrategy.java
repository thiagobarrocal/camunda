package com.example.workflow.service.email;

public interface EmailStrategy {
    void sendEmail(String emailTo, String message);
}
