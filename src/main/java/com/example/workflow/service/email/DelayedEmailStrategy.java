package com.example.workflow.service.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DelayedEmailStrategy implements EmailStrategy {

    private EmailSender emailSender;

    @Autowired
    public DelayedEmailStrategy(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String emailTo, String message) {
        emailSender.sendSimpleMessage(emailTo, "Approval Delayed", message);
        log.info("Sending delayed request to approval to email {}", emailTo);
    }
}
