package com.example.workflow.service;

import com.example.workflow.controller.dto.TravelRequestDTO;
import com.example.workflow.model.Travel;
import com.example.workflow.parser.TravelRequestMapper;
import com.example.workflow.repository.TravelRepository;
import com.example.workflow.utils.Checkers;
import com.example.workflow.utils.TravelStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TravelService {

    private final TravelRepository travelRepository;

    @Autowired
    public TravelService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public void createTravel(TravelRequestDTO travelRequestDTO) {
        checkConstraints(travelRequestDTO);
        travelRepository.save(TravelRequestMapper.INSTANCE.parseToEntity(travelRequestDTO));
    }

    private void checkConstraints(TravelRequestDTO travelRequestDTO) {
        Checkers.mustNotBeNull(travelRequestDTO, "The travel request must not be null");
        Checkers.mustNotBeBlank(travelRequestDTO.getTravelerName(), "The name of the traveler is required");
        Checkers.mustNotBeBlank(travelRequestDTO.getEmail(), "The email is required");
        Checkers.mustNotBeNull(travelRequestDTO.getAmount(), "The amount is required");
        Checkers.mustNotBeBlank(travelRequestDTO.getDepartment(), "The department is required");
    }

    @Async
    public void updateTravelStatusByEmail(String email, TravelStatusEnum status) {
        Optional<Travel> travel = travelRepository.findFirstByEmailOrderByIdDesc(email);
        if (travel.isEmpty()) {
            throw new IllegalArgumentException("The travel with email " + email + " does not exist");
        }

        var entity = TravelRequestMapper.INSTANCE.parseTravelToTravelStatus(travel.get(), status);
        travelRepository.save(entity);
    }
}
