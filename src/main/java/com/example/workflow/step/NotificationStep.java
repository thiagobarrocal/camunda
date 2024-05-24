package com.example.workflow.step;

import com.example.workflow.service.ApprovalEmailStrategy;
import com.example.workflow.service.EmailSender;
import com.example.workflow.service.DeniedEmailStrategy;
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
    private static final String VARIABLE_EMAIL_CUSTOMER = "email";

    private ApprovalEmailStrategy approvalEmailStrategy;
    private DeniedEmailStrategy deniedEmailStrategy;
    private final EmailSender emailSender;

    @Autowired
    public NotificationStep(EmailSender emailSender, ApprovalEmailStrategy approvalEmailStrategy, DeniedEmailStrategy deniedEmailStrategy) {
        this.emailSender = emailSender;
        this.approvalEmailStrategy = approvalEmailStrategy;
        this.deniedEmailStrategy = deniedEmailStrategy;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String recipient = (String) delegateExecution.getVariable(VARIABLE_EMAIL_CUSTOMER);
        String notificationType = (String) delegateExecution.getVariable(VARIABLE_NOTIFICATION_CHECKPOINT);
        notifyClient(recipient, notificationType);
        log.info("Sending notification to the customer with email: {} and status: {}", recipient, notificationType);
    }

    public void notifyClient(String recipient, String notificationType) {
        String message;
        switch (notificationType) {
            case "approved":
                emailSender.setEmailStrategy(approvalEmailStrategy);
                message = "Your request has been approved.";
                break;
            case "denied":
                emailSender.setEmailStrategy(deniedEmailStrategy);
                message = "Your request has been rejected.";
                break;
            default:
                throw new IllegalArgumentException("Unknown notification type: " + notificationType);
        }
        emailSender.sendEmail(recipient, message);
    }
}
