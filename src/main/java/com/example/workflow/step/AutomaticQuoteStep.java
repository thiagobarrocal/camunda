package com.example.workflow.step;

import jakarta.inject.Named;
import lombok.extern.java.Log;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Log
@Named("automaticQuoteStep")
@Component
public class AutomaticQuoteStep implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Sending automatic quote to the customer");
        //delegateExecution.setVariable("lateNotificationSent", true);
    }
}
