package com.example.demo.services;


import com.example.demo.entity.Employee;
import com.example.demo.entity.Indicators;
import com.example.demo.entity.enums.Category;
import com.example.demo.entity.enums.Phase;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.SuggestionRepository;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class IndicatorsService {

    private final EmployeeRepository employeeRepository;
    private final NotificationRepository notificationRepository;
    private final TaskRepository taskRepository;
    private final SuggestionRepository suggestionRepository;

    public IndicatorsService(EmployeeRepository employeeRepository, NotificationRepository notificationRepository,
                             TaskRepository taskRepository, SuggestionRepository suggestionRepository) {
        this.employeeRepository = employeeRepository;
        this.notificationRepository = notificationRepository;
        this.taskRepository = taskRepository;
        this.suggestionRepository = suggestionRepository;
    }

    public Indicators getIndicators(Long id) {
        checkIfEmployeeExists(id);
        Employee assignee = employeeRepository.findById(id).get();

        Indicators i = new Indicators();
        LocalDateTime timeRange = LocalDateTime.now().minusDays(28);

        i.setNumberTasksEmployee(taskRepository.countTasksByAssigneeIdAndCreationTimeAfter(id, timeRange));
        i.setNumberTasksEverybody(taskRepository.countTasksByCreationTimeAfter(timeRange));

        i.setNumberTasksEmployeeToDo(taskRepository.countTasksByAssigneeIdAndCategoryAndCreationTimeAfter(id, Category.TO_DO, timeRange));
        i.setNumberTasksEverybodyToDo(taskRepository.countTasksByCategoryAndCreationTimeAfter(Category.TO_DO, timeRange));
        i.setNumberTasksEmployeeDoing(taskRepository.countTasksByAssigneeIdAndCategoryAndCreationTimeAfter(id, Category.DOING, timeRange));
        i.setNumberTasksEverybodyDoing(taskRepository.countTasksByCategoryAndCreationTimeAfter(Category.DOING, timeRange));
        i.setNumberTasksEmployeeDone(taskRepository.countTasksByAssigneeIdAndCategoryAndCreationTimeAfter(id, Category.DONE, timeRange));
        i.setNumberTasksEverybodyDone(taskRepository.countTasksByCategoryAndCreationTimeAfter(Category.DONE, timeRange));

        i.setNumberTasksEmployeeDoneBeforeDeadline(taskRepository.countDoneTasksByAssigneeIdAndEndTimeIsLessThanDeadline(id));
        i.setNumberTasksEverybodyDoneBeforeDeadline(taskRepository.countDoneTasksByAssigneeIdAndEndTimeIsLessThanDeadline());

        i.setNumberSuggestionsEmployee(suggestionRepository.countSuggestionsByAuthorIdAndCreationTimeAfter(id, timeRange));
        i.setNumberSuggestionsEverybody(suggestionRepository.countSuggestionsByCreationTimeAfter(timeRange));

        i.setNumberSuggestionsEmployeeReported(suggestionRepository.countSuggestionsByAuthorIdAndPhaseAndCreationTimeAfter(id, Phase.REPORTED, timeRange));
        i.setNumberSuggestionsEverybodyReported(suggestionRepository.countSuggestionsByPhaseAndCreationTimeAfter(Phase.REPORTED, timeRange));
        i.setNumberSuggestionsEmployeeInImplementation(suggestionRepository.countSuggestionsByAuthorIdAndPhaseAndCreationTimeAfter(id, Phase.IN_IMPLEMENTATION, timeRange));
        i.setNumberSuggestionsEverybodyInImplementation(suggestionRepository.countSuggestionsByPhaseAndCreationTimeAfter(Phase.IN_IMPLEMENTATION, timeRange));
        i.setNumberSuggestionsEmployeeImplemented(suggestionRepository.countSuggestionsByAuthorIdAndPhaseAndCreationTimeAfter(id, Phase.IMPLEMENTED, timeRange));
        i.setNumberSuggestionsEverybodyImplemented(suggestionRepository.countSuggestionsByPhaseAndCreationTimeAfter(Phase.IMPLEMENTED, timeRange));

        i.setAverageTimeTasksEmployeeBetweenDeadlineAndEndTime(taskRepository.countAverageDifferenceBetweenDeadlineAndEndTime(id));
        i.setAverageTimeTasksEverybodyBetweenDeadlineAndEndTime(taskRepository.countAverageDifferenceBetweenDeadlineAndEndTime());

        i.setAverageTimeNotificationsEmployeeBetweenStartTimeAndCreationTime(notificationRepository.countAverageDifferenceBetweenStartTimeAndCreationTime(id));
        i.setAverageTimeNotificationsEverybodyBetweenStartTimeAndCreationTime(notificationRepository.countAverageDifferenceBetweenStartTimeAndCreationTime());

        i.setAverageTimeNotificationsEmployeeBetweenEndTimeAndStartTime(notificationRepository.countAverageDifferenceBetweenEndTimeAndStartTime(id));
        i.setAverageTimeNotificationsEverybodyBetweenEndTimeAndStartTime(notificationRepository.countAverageDifferenceBetweenEndTimeAndStartTime());

        return i;
    }

    private void checkIfEmployeeExists(Long id) {
        if (!employeeRepository.findById(id).isPresent()) {
            throw new NotFoundException("Such employee doesn't exist!");
        }
    }
}
