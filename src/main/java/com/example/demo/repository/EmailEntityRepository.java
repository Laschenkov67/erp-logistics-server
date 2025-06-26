package com.example.demo.repository;

import com.example.demo.entity.Emails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailEntityRepository extends JpaRepository<Emails, Long> {
    Optional<List<Emails>> findByEmailOrderByTimestampDesc(String email);
}
