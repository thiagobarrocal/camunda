package com.example.workflow.service.email;

import com.example.workflow.service.TravelService;
import com.example.workflow.utils.TravelStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeniedEmailStrategy implements EmailStrategy {

    private TravelService travelService;

    private EmailSender emailSender;

    @Autowired
    public DeniedEmailStrategy(TravelService travelService, EmailSender emailSender) {
        this.travelService = travelService;
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String emailTo, String message) {
        this.updateTravelStatusByEmailToDenied(emailTo);
        emailSender.sendSimpleMessage(emailTo, "Travel Denied", message);
        log.info("Sending rejection email to {}", emailTo);
    }

    public void updateTravelStatusByEmailToDenied(String email) {
        travelService.updateTravelStatusByEmail(email, TravelStatusEnum.DENIED);
    }
}
