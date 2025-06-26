package com.example.demo.repository;

import com.example.demo.entity.CurrentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentReportRepository extends JpaRepository<CurrentReport, Long> {
}
