package com.example.TaskScheduler.controllers;

import com.example.TaskScheduler.models.Task;
import com.example.TaskScheduler.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    private final TaskService taskService;
    private final Task taskPrototype;

    @Autowired
    public MainController(TaskService taskService, Task taskPrototype) {
        this.taskService = taskService;
        this.taskPrototype = taskPrototype;
    }

    @GetMapping("/{username}")
    public String getTasks(Model model, @PathVariable String username,
                           @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        model.addAttribute("username", sessionUsername);
        model.addAttribute("task", taskPrototype);
        model.addAttribute("showTask", taskService.getTasksForUser(sessionUsername));
        return "main";
    }

    @PostMapping("/{username}/task/addTask")
    public String addTask(@ModelAttribute("task") Task task, Model model, @PathVariable String username,
                          @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        model.addAttribute("username", sessionUsername);
        if (taskService.isTaskTitleEmpty(task.getTitle())) {
            model.addAttribute("task", taskPrototype);
            model.addAttribute("showTask", taskService.getTasksForUser(sessionUsername));
            model.addAttribute("error_title", "Порожній заголовок завдання");
            return "main";
        }
        if (taskService.isTaskDescriptionEmpty(task.getDescription())) {
            model.addAttribute("task", taskPrototype);
            model.addAttribute("showTask", taskService.getTasksForUser(sessionUsername));
            model.addAttribute("error_description", "Порожній текст завдання");
            return "main";
        }
        if (taskService.isTaskDeadlineEmpty(task.getDeadline())) {
            model.addAttribute("task", taskPrototype);
            model.addAttribute("showTask", taskService.getTasksForUser(sessionUsername));
            model.addAttribute("error_deadline", "Порожній дедлайн");
            return "main";
        }
        taskService.addTask(task, sessionUsername);
        return "redirect:/" + sessionUsername;
    }

    @PostMapping("/{username}/task/toggleCompletedTask/{id}")
    public String toggleCompleted(@PathVariable int id, @PathVariable String username,
                                  @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID: " + id));
        task.setCompleted(!task.isCompleted());
        taskService.updateTask(task);
        return "redirect:/" + sessionUsername;
    }

    @GetMapping("/{username}/editTask/{id}")
    public String editTask(@PathVariable int id, Model model, @PathVariable String username,
                           @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        model.addAttribute("username", sessionUsername);
        Task taskToEdit = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID:" + id));
        model.addAttribute("isEditing", id);
        model.addAttribute("task", taskPrototype);
        model.addAttribute("newTask", taskToEdit);
        model.addAttribute("showTask", taskService.getTasksForUser(sessionUsername));
        return "main";
    }

    @PostMapping("/{username}/task/updateTask")
    public String updateTask(@ModelAttribute("task") Task task, @PathVariable String username,
                             @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        taskService.updateTask(task);
        return "redirect:/" + sessionUsername;
    }

    @PostMapping("/{username}/task/deleteTask/{id}")
    public String deleteTask(@PathVariable int id, @PathVariable String username,
                             @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID: " + id));
        taskService.deleteTask(task);
        return "redirect:/" + sessionUsername;
    }

    @PostMapping("/{username}/tasks/sortByDate")
    public String getTasksSortedByDate(Model model, @PathVariable String username,
                                       @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        model.addAttribute("username", sessionUsername);
        model.addAttribute("task", taskPrototype);
        model.addAttribute("showTask", taskService.getTasksSortedByDate(sessionUsername));
        return "main";
    }

    @PostMapping("/{username}/tasks/sortByPriority")
    public String getTasksSortedByPriority(Model model, @PathVariable String username,
                                           @SessionAttribute("username") String sessionUsername) {
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        model.addAttribute("username", sessionUsername);
        model.addAttribute("task", taskPrototype);
        model.addAttribute("showTask", taskService.getTasksSortedByPriority(sessionUsername));
        return "main";
    }
}
