package com.example.workflow.parser;

import com.example.workflow.controller.dto.TravelRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;

@Mapper
public interface TravelRequestMapper {

    public static final String TYPE = "api";

    TravelRequestMapper INSTANCE = Mappers.getMapper(TravelRequestMapper.class);

    @Mapping(target = "class", ignore = true) // Ignora o campo "class"
    default HashMap<String, Object> toMap(TravelRequestDTO dto) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", TYPE);
        map.put("travelerName", dto.getTravelerName());
        map.put("email", dto.getEmail());
        map.put("travelerDocument", dto.getTravelerDocument());
        map.put("justification", dto.getJustification());
        map.put("origin", dto.getOrigin());
        map.put("destination", dto.getDestination());
        map.put("departureDate", dto.getDepartureDate());
        map.put("returnDate", dto.getReturnDate());
        map.put("amount", dto.getAmount());
        map.put("department", dto.getDepartment());
        return map;
    }
}
