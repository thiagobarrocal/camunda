package com.example.workflow.service;

import com.example.workflow.controller.dto.TravelRequestDTO;
import com.example.workflow.exception.StartProcessException;
import com.example.workflow.parser.TravelRequestMapper;
import lombok.extern.java.Log;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Log
@Service
public class ProcessService {

    private static final String START_PROCESS_ID = "main-flow";

    public void startProcess(TravelRequestDTO travelRequestDTO) {

        try {
            Map<String, Object> variables = TravelRequestMapper.INSTANCE.toMap(travelRequestDTO);
            //
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            runtimeService.startProcessInstanceByKey(START_PROCESS_ID, variables);
            //ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(START_PROCESS_ID, variables);
            //runtimeService.deleteProcessInstance(processInstance.getId(), null);
            log.info("Starting the process successfully");

        } catch (Exception e) {
            log.severe("Error starting the process: " + e.getMessage());
            throw  new StartProcessException("Error starting the initial process");
        }
    }

}
