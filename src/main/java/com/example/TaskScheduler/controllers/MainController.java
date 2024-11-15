package com.example.TaskScheduler.controllers;

import com.example.TaskScheduler.models.Task;
import com.example.TaskScheduler.service.TaskService;
import jakarta.servlet.http.HttpSession;
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

    private void displayUsername(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
    }

    @GetMapping("/{username}")
    public String getTasks(Model model, HttpSession session,
                           @PathVariable String username, @SessionAttribute("username") String sessionUsername) {
        username = (String) session.getAttribute("username");
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        displayUsername(model, session);
        model.addAttribute("task", taskPrototype);
        model.addAttribute("showTask", taskService.getTasksForUser(sessionUsername));
        return "main";
    }

    @PostMapping("/{username}/task/addTask")
    public String addTask(@ModelAttribute("task") Task task, Model model,
                          HttpSession session, @PathVariable String username,
                          @SessionAttribute("username") String sessionUsername) {
        username = (String) session.getAttribute("username");
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        displayUsername(model, session);
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
        return "redirect:/" + username;
    }

    @PostMapping("/{username}/task/toggleCompletedTask/{id}")
    public String toggleCompleted(@PathVariable int id, HttpSession session, @PathVariable String username,
                                  @SessionAttribute("username") String sessionUsername) {
        username = (String) session.getAttribute("username");
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID: " + id));
        task.setCompleted(!task.isCompleted());
        taskService.updateTask(task);
        return "redirect:/" + username;
    }

    @GetMapping("/{username}/editTask/{id}")
    public String editTask(@PathVariable int id, Model model,
                           HttpSession session, @PathVariable String username,
                           @SessionAttribute("username") String sessionUsername) {
        username = (String) session.getAttribute("username");
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        displayUsername(model, session);
        Task taskToEdit = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID:" + id));
        model.addAttribute("isEditing", id);
        model.addAttribute("task", taskPrototype);
        model.addAttribute("newTask", taskToEdit);
        model.addAttribute("showTask", taskService.getTasksForUser(sessionUsername));
        return "main";
    }

    @PostMapping("/{username}/task/updateTask")
    public String updateTask(@ModelAttribute("task") Task task, HttpSession session, @PathVariable String username,
                             @SessionAttribute("username") String sessionUsername) {
        username = (String) session.getAttribute("username");
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        taskService.updateTask(task);
        return "redirect:/" + username;
    }

    @PostMapping("/{username}/task/deleteTask/{id}")
    public String deleteTask(@PathVariable int id, HttpSession session, @PathVariable String username,
                             @SessionAttribute("username") String sessionUsername) {
        username = (String) session.getAttribute("username");
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        Task task = taskService.getTaskById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid task ID: " + id));
        taskService.deleteTask(task);
        return "redirect:/" + username;
    }

    @PostMapping("/{username}/tasks/sortByDate")
    public String getTasksSortedByDate(Model model, HttpSession session, @PathVariable String username,
                                       @SessionAttribute("username") String sessionUsername) {
        username = (String) session.getAttribute("username");
        displayUsername(model, session);
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        model.addAttribute("task", taskPrototype);
        model.addAttribute("showTask", taskService.getTasksSortedByDate(sessionUsername));
        return "main";
    }

    @PostMapping("/{username}/tasks/sortByPriority")
    public String getTasksSortedByPriority(Model model, HttpSession session, @PathVariable String username,
                                           @SessionAttribute("username") String sessionUsername) {
        username = (String) session.getAttribute("username");
        displayUsername(model, session);
        if (!username.equals(sessionUsername)) {
            return "redirect:/login";
        }
        model.addAttribute("task", taskPrototype);
        model.addAttribute("showTask", taskService.getTasksSortedByPriority(sessionUsername));
        return "main";
    }
}
