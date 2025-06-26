package com.example.demo.repository;

import com.example.demo.entity.SpecialPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialPlanRepository extends JpaRepository<SpecialPlan, Long> {
    Optional<List<SpecialPlan>> findByDay(LocalDate day);
}
