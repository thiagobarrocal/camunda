package com.example.workflow.service;

public interface EmailStrategy {
    void sendEmail(String emailTo, String message);
}
