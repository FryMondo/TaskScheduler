package com.example.TaskScheduler.controllers;

import com.example.TaskScheduler.models.User;
import com.example.TaskScheduler.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserService userService;
    private final User userPrototype;

    @Autowired
    public RegistrationController(UserService userService, User userPrototype) {
        this.userService = userService;
        this.userPrototype = userPrototype;
    }

    @GetMapping("/registration")
    public String getRegisteredUser(Model model) {
        model.addAttribute("user", userPrototype);
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute User user, HttpSession session, Model model) {
        if (userService.isUsernameEmpty(user.getUsername())) {
            model.addAttribute("error_username", "Порожнє ім'я");
            return "registration";
        }
        if (userService.isUsernameShort(user.getUsername())) {
            model.addAttribute("error_username", "Ім'я повинно бути мати хоча б 3 символи");
            return "registration";

        }
        if (userService.isUsernameHasSpace(user.getUsername())) {
            model.addAttribute("error_username", "Ім'я не повинно містити пробіли");
            return "registration";
        }
        if (userService.isUsernameTaken(user.getUsername())) {
            model.addAttribute("error_username", "Такий користувач існує");
            return "registration";
        }

        if (userService.isPasswordEmpty(user.getPassword())) {
            model.addAttribute("error_password", "Порожній пароль");
            return "registration";
        }
        if (userService.isPasswordShort(user.getPassword())) {
            model.addAttribute("error_password", "Пароль повинен містити хоча б 5 символів");
            return "registration";
        }
        if (userService.isPasswordHasSpace(user.getPassword())) {
            model.addAttribute("error_password", "Пароль не повинен містити пробіли");
            return "registration";
        }
        if (userService.isPasswordHasLower(user.getPassword())) {
            model.addAttribute("error_password", "Пароль повинен містити 1 маленьку літеру");
            return "registration";
        }
        if (userService.isPasswordHasUpper(user.getPassword())) {
            model.addAttribute("error_password", "Пароль повинен містити 1 велику літеру");
            return "registration";
        }
        userService.addUser(user);
        session.setAttribute("username", user.getUsername());
        return "redirect:/" + user.getUsername();
    }
}
