package se.lexicon.g48todoapi.service;

import se.lexicon.g48todoapi.domain.dto.TaskDTOForm;
import se.lexicon.g48todoapi.domain.dto.TaskDTOView;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskDTOView create(TaskDTOForm taskDTOForm);

    TaskDTOView findById(Long todoItemId);

    void update(TaskDTOForm taskDTOForm);

    void delete(Long id);

    List<TaskDTOView> findTasksByPersonId(Long personId);

    List<TaskDTOView> findTasksBetweenStartAndEndDate(LocalDate start, LocalDate end);

    List<TaskDTOView> findAllUnassignedTodoItems();

    List<TaskDTOView> findAllUnfinishedAndOverdue();

}
