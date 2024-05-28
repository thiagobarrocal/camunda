package com.example.workflow.steps;

import com.example.workflow.utils.Constants;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Named(Constants.STOP_STEP_BEAN_NAME)
@Component
public class StopStep implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        log.info("Stopping the process...");
    }
}
