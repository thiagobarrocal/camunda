package com.example.workflow.step;

import com.example.workflow.service.email.ApprovalEmailStrategy;
import com.example.workflow.service.email.EmailService;
import com.example.workflow.service.email.DeniedEmailStrategy;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Named("notificationStep")
@Component
@Slf4j
public class NotificationStep implements JavaDelegate {

    private static final String VARIABLE_NOTIFICATION_CHECKPOINT = "travelCheckpoint";
    private static final String VARIABLE_RESULT_CHECKPOINT = "result";
    private static final String VARIABLE_QUOTE_REFERENCE_ID = "quoteReferenceId";
    private static final String VARIABLE_EMAIL_CUSTOMER = "email";

    private ApprovalEmailStrategy approvalEmailStrategy;
    private DeniedEmailStrategy deniedEmailStrategy;
    private final EmailService emailService;

    @Autowired
    public NotificationStep(EmailService emailService, ApprovalEmailStrategy approvalEmailStrategy, DeniedEmailStrategy deniedEmailStrategy) {
        this.emailService = emailService;
        this.approvalEmailStrategy = approvalEmailStrategy;
        this.deniedEmailStrategy = deniedEmailStrategy;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String recipient = (String) delegateExecution.getVariable(VARIABLE_EMAIL_CUSTOMER);
        String notificationType = (String) delegateExecution.getVariable(VARIABLE_NOTIFICATION_CHECKPOINT);
        String result = (String) delegateExecution.getVariable(VARIABLE_RESULT_CHECKPOINT);
        String quoteReferenceId = (String) delegateExecution.getVariable(VARIABLE_QUOTE_REFERENCE_ID);

        if (notificationType == null && result != null && result.equals("automatic")) {
            notificationType = "approved";
        }
        notifyClient(recipient, notificationType, quoteReferenceId);
        log.info("Sending notification to the customer with email: {} and status: {}", recipient, notificationType);
    }

    public void notifyClient(String recipient, String notificationType, String quoteReferenceId) {
        String message;
        switch (notificationType) {
            case "approved":
                emailService.setEmailStrategy(approvalEmailStrategy);
                message = String.format("Your request has been approved! Quote reference id: %s", quoteReferenceId);
                break;
            case "denied":
                emailService.setEmailStrategy(deniedEmailStrategy);
                message = "Your request has been rejected.";
                break;
            default:
                throw new IllegalArgumentException("Unknown notification type: " + notificationType);
        }
        emailService.sendEmail(recipient, message);
    }
}
