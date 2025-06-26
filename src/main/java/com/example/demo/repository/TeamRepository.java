package com.example.demo.repository;

import com.example.demo.entity.Team;
import com.example.demo.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByManagerId(Long id);

    Team findByRole(Role role);
}
