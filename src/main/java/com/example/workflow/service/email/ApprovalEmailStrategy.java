package com.example.workflow.service.email;


import com.example.workflow.service.TravelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApprovalEmailStrategy implements EmailStrategy {

    private EmailSender emailSender;

    @Autowired
    public ApprovalEmailStrategy(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String emailTo, String message) {
        emailSender.sendSimpleMessage(emailTo, "Travel Approved", message);
    }
}
