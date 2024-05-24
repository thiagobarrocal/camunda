package com.example.workflow.service;

import com.example.workflow.utils.TravelStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeniedEmailStrategy implements EmailStrategy {

    private TravelService travelService;

    @Autowired
    public DeniedEmailStrategy(TravelService travelService) {
        this.travelService = travelService;
    }

    @Override
    public void sendEmail(String emailTo, String message) {
        updateTravelStatusByEmailToDenied(emailTo);
        // send email
        log.info("Sending rejection email to {}", emailTo);

    }

    public void updateTravelStatusByEmailToDenied(String email) {
        travelService.updateTravelStatusByEmail(email, TravelStatusEnum.DENIED);
    }
}
