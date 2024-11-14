package com.example.TaskScheduler.service;

import com.example.TaskScheduler.data.TaskRepositoryStub;
import com.example.TaskScheduler.models.Task;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
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

    public List<Task> getTasksForUser(int userId) {
        return taskRepositoryStub.getTasks().stream()
                .filter(task -> task.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public void addTask(Task task, int userId) {
        task.setId(++taskID);
        task.setUserId(userId);
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

    public List<Task> getTasksSortedByDate(int userId) {
        return taskRepositoryStub.getTasks().stream()
                .filter(task -> task.getUserId() == userId)
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    public List<Task> getTasksSortedByPriority(int userId) {
        return taskRepositoryStub.getTasks().stream()
                .filter(task -> task.getUserId() == userId)
                .sorted(Comparator.comparing(Task::getPriority))
                .collect(Collectors.toList());
    }
}