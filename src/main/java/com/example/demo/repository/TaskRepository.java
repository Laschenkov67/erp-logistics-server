package com.example.demo.repository;

import com.example.demo.entity.Task;
import com.example.demo.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    long countTasksByAssigneeIdAndCreationTimeAfter(Long id, LocalDateTime timeRange);

    long countTasksByCreationTimeAfter(LocalDateTime timeRange);

    long countTasksByAssigneeIdAndCategoryAndCreationTimeAfter(Long id, Category category, LocalDateTime timeRange);

    long countTasksByCategoryAndCreationTimeAfter(Category category, LocalDateTime timeRange);

    @Query(value = "SELECT COUNT(t.id) FROM task t WHERE t.assignee_id = :assignee_id AND t.category = 'DONE' AND" +
            " t.end_time <= t.deadline", nativeQuery = true)
    Long countDoneTasksByAssigneeIdAndEndTimeIsLessThanDeadline(@Param("assignee_id") Long assignee_id);

    @Query(value = "SELECT COUNT(t.id) FROM task t WHERE t.category = 'DONE' AND t.end_time <= t.deadline",
            nativeQuery = true)
    Long countDoneTasksByAssigneeIdAndEndTimeIsLessThanDeadline();

    @Query(value = "SELECT EXTRACT(EPOCH FROM (SELECT avg(t.deadline - t.end_time) FROM task t WHERE" +
            " t.category = 'DONE' AND t.assignee_id = :assignee_id))", nativeQuery = true)
    Long countAverageDifferenceBetweenDeadlineAndEndTime(@Param("assignee_id") Long assignee_id);

    @Query(value = "SELECT EXTRACT(EPOCH FROM (SELECT avg(t.deadline - t.end_time) FROM task t WHERE" +
            " t.category = 'DONE'))", nativeQuery = true)
    Long countAverageDifferenceBetweenDeadlineAndEndTime();

    Optional<List<Task>> findTasksByAssigneeIdAndCreationTimeAfterOrderByDeadlineAsc(Long id,LocalDateTime timeRange);

    Optional<List<Task>> findTaskByAssigneeIsNull();
}
