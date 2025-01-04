package com.example.TaskScheduler.controllers;

import com.example.TaskScheduler.models.Task;
import com.example.TaskScheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {
    private final TaskService taskService;
    private final Task taskPrototype;

    @Autowired
    public MainController(TaskService taskService, Task taskPrototype) {
        this.taskService = taskService;
        this.taskPrototype = taskPrototype;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Task>> getTasks(Model model, @PathVariable String username,
                                   @SessionAttribute(value = "username", required = false) String sessionUsername,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   @RequestParam(defaultValue = "all") String filterStatus) {
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        List<Task> tasks;

        if (filterStatus.equals("completed")) {
            tasks = taskService.getCompletedTasksForUser(username, page, size);
        } else if (filterStatus.equals("notCompleted")) {
            tasks = taskService.getNotCompletedTasksForUser(username, page, size);
        } else {
            tasks = taskService.getPaginatedTasks(username, page, size);
        }

        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/{username}/task/addTask")
    public ResponseEntity<Void> addTask(@ModelAttribute("task") Task task, Model model, @PathVariable String username,
                          @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        if (taskService.isTaskTitleEmpty(task.getTitle())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (taskService.isTaskDescriptionEmpty(task.getDescription())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (taskService.isTaskDeadlineEmpty(task.getDeadline())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        taskService.addTask(task, sessionUsername);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{username}/task/toggleCompletedTask/{id}")
    public ResponseEntity<Task> toggleCompleted(@PathVariable int id, @PathVariable String username,
                                  @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        Task task = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID: " + id));

        task.setCompleted(!task.isCompleted());
        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{username}/editTask/{id}")
    public ResponseEntity<Task> editTask(@PathVariable int id, Model model, @PathVariable String username,
                           @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        Task taskToEdit = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID:" + id));

        return ResponseEntity.ok(taskToEdit);
    }

    @PostMapping("/{username}/task/updateTask")
    public ResponseEntity<Task> updateTask(@ModelAttribute("task") Task task, @PathVariable String username,
                             @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        taskService.updateTask(task);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{username}/task/deleteTask/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id, @PathVariable String username,
                             @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        Task task = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID: " + id));

        taskService.deleteTask(task);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{username}/tasks/sortByDate")
    public ResponseEntity<List<Task>> getTasksSortedByDate(Model model, @PathVariable String username,
                                       @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        List<Task> sortedTasks = taskService.getTasksSortedByDate(sessionUsername);

        return ResponseEntity.status(HttpStatus.OK).body(sortedTasks);
    }

    @PostMapping("/{username}/tasks/sortByPriority")
    public ResponseEntity<List<Task>> getTasksSortedByPriority(Model model, @PathVariable String username,
                                           @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        List<Task> sortedTasks = taskService.getTasksSortedByPriority(sessionUsername);

        return ResponseEntity.status(HttpStatus.OK).body(sortedTasks);
    }

    @GetMapping("/{username}/tasks{page}")
    public ResponseEntity<Map<String, Object>> getPaginatedTasks(@PathVariable String username,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int size,
                                                                 @SessionAttribute("username") String sessionUsername) {

        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 5;
        }

        List<Task> paginatedTasks = taskService.getPaginatedTasks(username, page, size);
        int totalTasks = taskService.getTotalTaskCount(username);

        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalTasks", totalTasks);
        response.put("tasks", paginatedTasks);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
