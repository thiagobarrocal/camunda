package com.example.workflow.service;

import com.example.workflow.controller.dto.TravelRequestDTO;
import com.example.workflow.exception.StartProcessException;
import com.example.workflow.parser.TravelRequestMapper;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
public class ProcessService {

    private TravelService travelService;

    @Autowired
    public ProcessService(TravelService travelService) {
        this.travelService = travelService;
    }

    private static final String START_PROCESS_ID = "main-flow";

    public void startProcess(TravelRequestDTO travelRequestDTO) {
        try {
            // Save the travel request asynchronously
            saveTravelRequest(travelRequestDTO);

            Map<String, Object> variables = TravelRequestMapper.INSTANCE.toMap(travelRequestDTO);
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            runtimeService.startProcessInstanceByKey(START_PROCESS_ID, variables);
            //ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(START_PROCESS_ID, variables);
            //runtimeService.deleteProcessInstance(processInstance.getId(), null);
            log.info("Starting the process '{}' successfully", START_PROCESS_ID);

        } catch (Exception e) {
            log.error("Error starting the process: " + e.getMessage());
            throw  new StartProcessException("Error starting the initial process");
        }
    }

    @Async
    public void saveTravelRequest(TravelRequestDTO travelRequestDTO) {
        travelService.createTravel(travelRequestDTO);
    }

}
