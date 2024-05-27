package com.example.workflow.service;

import com.example.workflow.controller.dto.CancelProcessDto;
import com.example.workflow.controller.dto.TravelRequestDTO;
import com.example.workflow.exception.CamundaProcessException;
import com.example.workflow.parser.TravelRequestMapper;
import com.example.workflow.utils.Checkers;
import com.example.workflow.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ProcessService {

    private final TravelService travelService;

    @Autowired
    public ProcessService(TravelService travelService) {
        this.travelService = travelService;
    }

    public void startProcess(TravelRequestDTO travelRequestDTO) {
        try {
            saveTravelRequest(travelRequestDTO);

            Map<String, Object> variables = TravelRequestMapper.INSTANCE.toMap(travelRequestDTO);
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            runtimeService.startProcessInstanceByKey(Constants.START_PROCESS_ID, variables);
            log.info("Starting the process '{}' successfully", Constants.START_PROCESS_ID);

        } catch (Exception e) {
            log.error("Error starting the process: " + e.getMessage());
            throw  new CamundaProcessException("Error starting the initial process");
        }
    }

    public void cancelProcess(CancelProcessDto cancelProcessDto) {
        try {
            Checkers.mustNotBeNull(cancelProcessDto, "The cancelProcessDto request must not be null");
            Checkers.mustNotBeNull(cancelProcessDto.getProcessId(), "The process id must not be null");

            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            runtimeService.deleteProcessInstance(cancelProcessDto.getProcessId(), null);
            log.info("Process '{}' canceled successfully", cancelProcessDto.getProcessId());

        } catch (Exception e) {
            log.error("Error canceling the process id '{}' error: {}", cancelProcessDto.getProcessId(), e.getMessage());
            throw  new CamundaProcessException("Error canceling the process");
        }
    }

    @Async
    public void saveTravelRequest(TravelRequestDTO travelRequestDTO) {
        travelService.createTravel(travelRequestDTO);
    }

}
