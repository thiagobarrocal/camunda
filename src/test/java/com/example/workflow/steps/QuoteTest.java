package com.example.workflow.steps;

import com.example.workflow.gateway.QuoteApiClient;
import com.example.workflow.gateway.dto.QuoteApiClientResponse;
import com.example.workflow.service.NotificationService;
import com.example.workflow.service.TravelService;
import com.example.workflow.service.email.EmailSender;
import com.example.workflow.service.email.EmailService;
import com.example.workflow.utils.Constants;
import com.example.workflow.utils.TravelStatusEnum;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuoteTest {

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension.builder().build();

    @Mock
    private QuoteApiClient quoteApiClient;

    @Mock
    private TravelService travelService;

    @Mock
    private EmailService emailService;

    @Mock
    private EmailSender emailSender;

    @Mock
    private NotificationService notificationService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        QuoteStep quoteStep = new QuoteStep(quoteApiClient, travelService);
        Mocks.register("quoteStep", quoteStep);
    }

    @Test
    @DisplayName("Test Flow when travel request is manual coz amount >= 1000 and department is sales")
    @Deployment(resources = {"processes/flow.bpmn", "processes/dmn.dmn", "processes/form1.form", "processes/form2.form"})
    public void flowStopingInManual() {
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("main-flow", createVariables());
        assertThat(processInstance).isActive();
        ActivityInstance activityInstance = runtimeService().getActivityInstance(processInstance.getId());
        List<String> activeActivityNames = getActiveActivityNames(activityInstance);
        Assertions.assertTrue(activeActivityNames.contains("manual"));

        // When we complete that task
        // complete(task(processInstance));
        // Then the process instance should be ended
        //assertThat(processInstance).isEnded();

        //assertThat(processInstance).isEnded();
        //assertThat(processInstance).isStarted().isNotEnded();
    }

    @Test
    @DisplayName("Test Happy Path Flow when has automatic approval and automatic quote")
    @Deployment(resources = {"processes/flow.bpmn", "processes/dmn.dmn", "processes/form1.form", "processes/form2.form"})
    public void flowWithAutomaticApprovals_sales_500() {
        var variables = createVariables();
        variables.put("departmente", "sales");
        variables.put("amount", 500);
        variables.put("manualQuoteReferenceId", "37ec13ad-1c39-46dc-a6a0-a5bc917a1e07");

        doNothing().when(travelService).updateTravelQuoteIdByEmail(anyString(), anyString());
        doNothing().when(travelService).updateTravelStatusByEmail(anyString(), any(TravelStatusEnum.class));
        when(quoteApiClient.execute(any())).thenReturn(new QuoteApiClientResponse());
        doNothing().when(notificationService).notify(anyString(), anyString(), anyString());
        doNothing().when(emailService).sendEmail(anyString(), anyString());
        doNothing().when(emailSender).sendSimpleMessage(anyString(), anyString(), anyString());

        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("main-flow", variables);
        assertThat(processInstance).isEnded();
    }

    @Test
    @DisplayName("Test with Quote exception stopping in manual quote step")
    @Deployment(resources = {"processes/flow.bpmn", "processes/dmn.dmn", "processes/form1.form", "processes/form2.form"})
    public void flowWithmanualQuote() {
        var variables = createVariables();
        variables.put("departmente", "sales");
        variables.put("amount", 500);

        when(quoteApiClient.execute(any())).thenThrow(new BpmnError(Constants.QUOTE_API_ERROR_CODE, "Error calling quote api"));
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("main-flow", variables);
        assertThat(processInstance).isActive();
        ActivityInstance activityInstance = runtimeService().getActivityInstance(processInstance.getId());
        List<String> activeActivityNames = getActiveActivityNames(activityInstance);
        Assertions.assertTrue(activeActivityNames.contains("manual quote"));
    }

    @Test
    @DisplayName("Test with Manual approve setting to need change choice")
    @Deployment(resources = {"processes/flow.bpmn", "processes/dmn.dmn", "processes/form1.form", "processes/form2.form"})
    public void flowWithManualApprovelAfterNeedChangeFlow() {
        var variables = createVariables();
        variables.put("departmente", "marketing");
        variables.put("amount", 4000);

        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("main-flow", variables);
        assertThat(processInstance).isActive();
        assertThat(processInstance).isStarted().isNotEnded();
        ActivityInstance activityInstance = runtimeService().getActivityInstance(processInstance.getId());
        List<String> activeActivityNames = getActiveActivityNames(activityInstance);
        Assertions.assertTrue(activeActivityNames.contains("manual"));

        Task manualTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("manual")
                .singleResult();
        assertThat(manualTask).isNotNull();

        //Claim the task
        taskService.claim(manualTask.getId(), "testUser");

        // Complete the task with denied choice
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("travelCheckpoint", "change");
        taskService.complete(manualTask.getId(), taskVariables);

        Task needChangesManualTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("need changes")
                .singleResult();
        assertThat(needChangesManualTask).isNotNull();
    }

    @Test
    @DisplayName("Test with Manual approve setting to denied choice")
    @Deployment(resources = {"processes/flow.bpmn", "processes/dmn.dmn", "processes/form1.form", "processes/form2.form"})
    public void flowWithManualApprovelAfterDeniedFlow() {
        var variables = createVariables();
        variables.put("departmente", "marketing");
        variables.put("amount", 4000);

        doNothing().when(notificationService).notify(anyString(), anyString(), anyString());
        doNothing().when(emailService).sendEmail(anyString(), anyString());
        doNothing().when(emailSender).sendSimpleMessage(anyString(), anyString(), anyString());

        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("main-flow", variables);
        assertThat(processInstance).isActive();
        assertThat(processInstance).isStarted().isNotEnded();
        ActivityInstance activityInstance = runtimeService().getActivityInstance(processInstance.getId());
        List<String> activeActivityNames = getActiveActivityNames(activityInstance);
        Assertions.assertTrue(activeActivityNames.contains("manual"));

        Task manualTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .taskName("manual")
                .singleResult();
        assertThat(manualTask).isNotNull();

        //Claim the task
        taskService.claim(manualTask.getId(), "testUser");

        // Complete the task with denied choice
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("travelCheckpoint", "denied");
        taskService.complete(manualTask.getId(), taskVariables);
        assertThat(processInstance).isEnded();
    }

    private List<String> getActiveActivityNames(ActivityInstance activityInstance) {
        return Arrays.stream(activityInstance.getChildActivityInstances())
                .map(ActivityInstance::getActivityName)
                .collect(Collectors.toList());
    }

    private Map<String, Object> createVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("type", "api");
        variables.put("travelerName", "Thiago");
        variables.put("email", "thiagobarrocal@gmail.com");
        variables.put("origin", "São Paulo");
        variables.put("destination", "Rio de Janeiro");
        variables.put("departureDate", "2021-12-01");
        variables.put("departureDate", "2021-12-01");
        variables.put("amount", 1500);
        variables.put("department", "sales");
        return variables;
    }

}
