package com.example.workflow.step;

import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Named("ajustmentsStep")
@Component
public class AjustmentsStep implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Sending ajustments to the customer");
        //delegateExecution.setVariable("lateNotificationSent", true);
    }
}
