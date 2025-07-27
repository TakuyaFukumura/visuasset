package com.example.visuasset.repository;

import com.example.visuasset.entity.SimulationCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulationConditionRepository extends JpaRepository<SimulationCondition, Long> {
}