package com.example.demo.repository;

import com.example.demo.entity.Suggestion;
import com.example.demo.entity.enums.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    long countSuggestionsByAuthorIdAndCreationTimeAfter(Long id, LocalDateTime timeRange);

    long countSuggestionsByCreationTimeAfter(LocalDateTime timeRange);

    long countSuggestionsByAuthorIdAndPhaseAndCreationTimeAfter(Long id, Phase phase, LocalDateTime timeRange);

    long countSuggestionsByPhaseAndCreationTimeAfter(Phase phase, LocalDateTime timeRange);

    Optional<List<Suggestion>> findByRecipientsId(Long id);

    Optional<List<Suggestion>> findByAuthorId(Long id);
}
