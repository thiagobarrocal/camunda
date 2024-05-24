package com.example.workflow.service;


import com.example.workflow.utils.TravelStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApprovalEmailStrategy implements EmailStrategy {

    private TravelService travelService;

    @Autowired
    public ApprovalEmailStrategy(TravelService travelService) {
        this.travelService = travelService;
    }

    @Override
    public void sendEmail(String emailTo, String message) {
        updateTravelStatusByEmailToApproved(emailTo);
        // send email
        log.info("Sending approval email to {}", emailTo);

    }

    public void updateTravelStatusByEmailToApproved(String email) {
        travelService.updateTravelStatusByEmail(email, TravelStatusEnum.APPROVED);
    }
}
