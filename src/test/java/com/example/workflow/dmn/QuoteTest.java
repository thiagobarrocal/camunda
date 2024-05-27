package com.example.workflow.dmn;

import com.example.workflow.gateway.QuoteApiClient;
import com.example.workflow.service.TravelService;
import com.example.workflow.step.QuoteStep;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuoteTest {

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension.builder().build();

    //@InjectMocks
    //private ClassABC classABC;

    @Mock
    private QuoteApiClient quoteApiClient;

    @Mock
    private TravelService travelService;

    @BeforeEach
    void setup() {
        QuoteStep quoteStep = new QuoteStep(quoteApiClient, travelService);
        Mocks.register("quoteStep", quoteStep);
    }

    @Test
    @DisplayName("Test Flow when travel request is manual coz amount >= 1000 and department is sales")
    @Deployment(resources = {"processes/flow.bpmn", "processes/dmn.dmn", "processes/form1.form", "processes/form2.form"})
    public void test() {

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

        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("main-flow", variables);
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

    private List<String> getActiveActivityNames(ActivityInstance activityInstance) {
        return Arrays.stream(activityInstance.getChildActivityInstances())
                .map(ActivityInstance::getActivityName)
                .collect(Collectors.toList());
    }

}
