package com.example.workflow.step;

import com.example.workflow.service.ApprovalEmailStrategy;
import com.example.workflow.service.EmailSender;
import com.example.workflow.service.RejectionEmailStrategy;
import jakarta.inject.Named;
import lombok.extern.java.Log;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Log
@Named("notificationStep")
@Component
public class NotificationStep implements JavaDelegate {

    private static final String VARIABLE_NOTIFICATION_CHECKPOINT = "travelCheckpoint";
    private static final String VARIABLE_EMAIL_CUSTOMER = "email";

    private final EmailSender emailSender;

    public NotificationStep(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String recipient = (String) delegateExecution.getVariable(VARIABLE_EMAIL_CUSTOMER);
        String notificationType = (String) delegateExecution.getVariable(VARIABLE_NOTIFICATION_CHECKPOINT);
        notifyClient(recipient, notificationType);
        log.info("Sending notification to the customer");
        //delegateExecution.setVariable("lateNotificationSent", true);
    }

    public void notifyClient(String recipient, String notificationType) {
        String message;
        switch (notificationType) {
            case "approved":
                emailSender.setEmailStrategy(new ApprovalEmailStrategy());
                message = "Your request has been approved.";
                break;
            case "denied":
                emailSender.setEmailStrategy(new RejectionEmailStrategy());
                message = "Your request has been rejected.";
                break;
            default:
                throw new IllegalArgumentException("Unknown notification type: " + notificationType);
        }
        emailSender.sendEmail(recipient, message);
    }
}
