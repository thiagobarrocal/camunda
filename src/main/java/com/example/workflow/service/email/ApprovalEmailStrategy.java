package com.example.workflow.service.email;


import com.example.workflow.service.TravelService;
import com.example.workflow.utils.TravelStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApprovalEmailStrategy implements EmailStrategy {

    private TravelService travelService;

    private EmailSender emailSender;

    @Autowired
    public ApprovalEmailStrategy(TravelService travelService, EmailSender emailSender) {
        this.travelService = travelService;
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String emailTo, String message) {
        this.updateTravelStatusByEmailToApproved(emailTo);
        emailSender.sendSimpleMessage(emailTo, "Travel Approved", message);
        log.info("Sending approval email to {}", emailTo);
    }

    public void updateTravelStatusByEmailToApproved(String email) {
        travelService.updateTravelStatusByEmail(email, TravelStatusEnum.APPROVED);
    }
}
