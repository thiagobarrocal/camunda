package com.example.workflow.repository;

import com.example.workflow.model.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    Optional<Travel> findFirstByEmailOrderByIdDesc(String email);

}
