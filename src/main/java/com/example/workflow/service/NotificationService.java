package com.example.workflow.service;

import com.example.workflow.service.email.ApprovalEmailStrategy;
import com.example.workflow.service.email.DelayedEmailStrategy;
import com.example.workflow.service.email.DeniedEmailStrategy;
import com.example.workflow.service.email.EmailService;
import com.example.workflow.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final ApprovalEmailStrategy approvalEmailStrategy;
    private final DeniedEmailStrategy deniedEmailStrategy;
    private final DelayedEmailStrategy delayedEmailStrategy;
    private final EmailService emailService;

    @Autowired
    public NotificationService(EmailService emailService,
                               ApprovalEmailStrategy approvalEmailStrategy,
                               DeniedEmailStrategy deniedEmailStrategy,
                               DelayedEmailStrategy delayedEmailStrategy) {
        this.emailService = emailService;
        this.approvalEmailStrategy = approvalEmailStrategy;
        this.deniedEmailStrategy = deniedEmailStrategy;
        this.delayedEmailStrategy = delayedEmailStrategy;
    }

    public void notify(String recipient, String notificationType, String quoteReferenceId) {
        String message;
        switch (notificationType) {
            case Constants.NOTIFICATION_TYPE_APPROVED:
                emailService.setEmailStrategy(approvalEmailStrategy);
                message = String.format("Your request has been approved! Quote reference id: %s", quoteReferenceId);
                break;
            case Constants.NOTIFICATION_TYPE_DENIED:
                emailService.setEmailStrategy(deniedEmailStrategy);
                message = "Your request has been rejected.";
                break;
            case Constants.NOTIFICATION_TYPE_DELAYED:
                emailService.setEmailStrategy(delayedEmailStrategy);
                message = "You have delayed approvals to do.";
                break;
            default:
                throw new IllegalArgumentException("Unknown notification type: " + notificationType);
        }
        emailService.sendEmail(recipient, message);
    }

    public void notify(String recipient, String notificationType) {
        notify(recipient, notificationType, null);
    }
}
