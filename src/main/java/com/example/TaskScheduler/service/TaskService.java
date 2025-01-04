package com.example.TaskScheduler.service;

import com.example.TaskScheduler.data.TaskRepositoryStub;
import com.example.TaskScheduler.models.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Scope("singleton")
public class TaskService {
    private static int taskID = 0;
    private final TaskRepositoryStub taskRepositoryStub;

    public TaskService(TaskRepositoryStub taskRepositoryStub) {
        this.taskRepositoryStub = taskRepositoryStub;
    }

    public boolean isTaskTitleEmpty(String title) {
        return title.trim().isEmpty();
    }

    public boolean isTaskDescriptionEmpty(String description) {
        return description.trim().isEmpty();
    }

    public boolean isTaskDeadlineEmpty(String deadline) {
        return deadline.trim().isEmpty();
    }

    public List<Task> getTasksForUser(String username) {
        return taskRepositoryStub.getTasks().stream()
                .filter(task -> Objects.equals(task.getUsername(), username))
                .collect(Collectors.toList());
    }

    public void addTask(Task task, String username) {
        task.setId(++taskID);
        task.setUsername(username);
        taskRepositoryStub.getTasks().add(task);
    }

    public Optional<Task> getTaskById(int id) {
        return taskRepositoryStub.getTasks().stream()
                .filter(task -> task.getId() == id)
                .findFirst();
    }

    public void updateTask(Task updatedTask) {
        getTaskById(updatedTask.getId()).ifPresent(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setDeadline(updatedTask.getDeadline());
            task.setPriority(updatedTask.getPriority());
        });
    }

    public void deleteTask(Task task) {
        taskRepositoryStub.getTasks().remove(task);
    }

    public List<Task> getTasksSortedByDate(String username) {
        return taskRepositoryStub.getTasks().stream()
                .filter(task -> Objects.equals(task.getUsername(), username))
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksSortedByPriority(String username) {
        return taskRepositoryStub.getTasks().stream()
                .filter(task -> Objects.equals(task.getUsername(), username))
                .sorted(Comparator.comparing(Task::getPriority))
                .collect(Collectors.toList());
    }

    public List<Task> getPaginatedTasks(String username, int page, int size) {
        return taskRepositoryStub.getTasksWithPagination(page, size).stream()
                .filter(task -> task.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public int getTotalTaskCount(String username) {
        // Фільтруємо завдання, які належать конкретному користувачеві, і рахуємо їх
        return (int) taskRepositoryStub.getTasks().stream()
                .filter(task -> task.getUsername().equals(username))
                .count();
    }
}