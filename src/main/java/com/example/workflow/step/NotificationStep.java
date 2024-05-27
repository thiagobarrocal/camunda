package com.example.workflow.step;

import com.example.workflow.service.NotificationService;
import com.example.workflow.utils.Constants;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Named(Constants.NOTIFICATION_STEP_BEAN_NAME)
@Component
@Slf4j
public class NotificationStep implements JavaDelegate {

    private final NotificationService notificationService;

    @Autowired
    public NotificationStep(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String recipient = (String) delegateExecution.getVariable(Constants.VARIABLE_EMAIL_CUSTOMER);
        String notificationType = (String) delegateExecution.getVariable(Constants.VARIABLE_NOTIFICATION_CHECKPOINT);
        String result = (String) delegateExecution.getVariable(Constants.VARIABLE_RESULT_CHECKPOINT);
        String quoteReferenceId = (String) delegateExecution.getVariable(Constants.VARIABLE_QUOTE_REFERENCE_ID);
        String quoteReferenceIdFromManualStep = (String) delegateExecution.getVariable(Constants.VARIABLE_MANUAL_QUOTE_REFERENCE_ID);

        if (quoteReferenceId == null) {
            quoteReferenceId = quoteReferenceIdFromManualStep;
        }

        if (notificationType == null && result != null && result.equals(Constants.FLOW_TYPE_AUTOMATIC)) {
            notificationType = Constants.NOTIFICATION_TYPE_APPROVED;

        notificationService.notify(recipient, notificationType, quoteReferenceId);}
        log.info("Sending notification to the customer with email: {} and status: {}", recipient, notificationType);
    }
}
