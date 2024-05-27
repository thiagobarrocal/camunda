package com.example.workflow.step;

import com.example.workflow.service.NotificationService;
import com.example.workflow.utils.Constants;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Named(Constants.DELAYED_APPROVAL_STEP_BEAN_NAME)
@Component
public class DelayedApprovalStep implements JavaDelegate {

    private final NotificationService notificationService;

    @Autowired
    public DelayedApprovalStep(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        var user = identityService.createUserQuery().memberOfGroup(Constants.GROUP_ID).userId(Constants.APPROVER_ID).singleResult();

        notificationService.notify(user.getEmail(), Constants.DELAYED);
        log.info("Sending notification to approve with delayed time");
    }
}
