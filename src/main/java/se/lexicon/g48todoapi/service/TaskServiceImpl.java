package se.lexicon.g48todoapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.g48todoapi.domain.dto.TaskDTOForm;
import se.lexicon.g48todoapi.domain.dto.TaskDTOView;
import se.lexicon.g48todoapi.domain.entity.Task;
import se.lexicon.g48todoapi.exception.DataNotFoundException;
import se.lexicon.g48todoapi.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDTOView create(TaskDTOForm taskDTOForm) {
        // Create a new Task entity using the DTO
        Task task = Task.builder()
                .title(taskDTOForm.getTitle())
                .description(taskDTOForm.getDescription())
                .deadline(taskDTOForm.getDeadline())
                .done(taskDTOForm.isDone())
                .build();

        // Save the created entity to the database
        Task savedTask = taskRepository.save(task);

        // Convert the saved entity to a DTO
        return TaskDTOView.builder()
                .id(savedTask.getId())
                .title(savedTask.getTitle())
                .description(savedTask.getDescription())
                .deadline(savedTask.getDeadline())
                .done(savedTask.isDone())
                .build();
    }

    @Override
    public TaskDTOView findById(Long taskId) {
        // Retrieve a Task entity by its ID
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new DataNotFoundException("Task not found"));

        // Convert the entity to a DTO
        return TaskDTOView.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .done(task.isDone())
                .build();
    }

    @Override
    public void update(TaskDTOForm taskDTOForm) {
        // Check if the Task exists
        Task existingTask = taskRepository.findById(taskDTOForm.getId())
                .orElseThrow(() -> new DataNotFoundException("Task not found"));

        // Update the existing entity using the DTO
        existingTask.setTitle(taskDTOForm.getTitle());
        existingTask.setDescription(taskDTOForm.getDescription());
        existingTask.setDeadline(taskDTOForm.getDeadline());
        existingTask.setDone(taskDTOForm.isDone());

        // Save the updated entity to the database
        taskRepository.save(existingTask);
    }

    @Override
    public void delete(Long id) {
        // Delete a TodoItem by its ID
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTOView> findTasksByPersonId(Long personId) {
        // Retrieve tasks associated with a person by their ID
        List<Task> taskList = taskRepository.findByPerson_Id(personId);
        return taskList.stream()
                .map(this::convertToTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findTasksBetweenStartAndEndDate(LocalDate start, LocalDate end) {
        // Retrieve tasks with deadlines between the given dates
        List<Task> taskList = taskRepository.findByDeadlineBetween(start, end);
        return taskList.stream()
                .map(this::convertToTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findAllUnassignedTodoItems() {
        // Retrieve all unassigned tasks
        List<Task> taskList = taskRepository.findByPersonIsNull();
        return taskList.stream()
                .map(this::convertToTaskDTOView)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTOView> findAllUnfinishedAndOverdue() {
        // Retrieve all unfinished and overdue tasks
        List<Task> taskList = taskRepository.selectUnFinishedAndOverdueTasks();
        return taskList.stream()
                .map(this::convertToTaskDTOView)
                .collect(Collectors.toList());
    }

    private TaskDTOView convertToTaskDTOView(Task task) {
        return TaskDTOView.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .done(task.isDone())
                .build();
    }

}
