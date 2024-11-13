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

import java.util.Optional;

@Controller
public class LoginController {
    private final UserService userService;
    private final User userPrototype;

    @Autowired
    public LoginController(UserService userService, User userPrototype) {
        this.userService = userService;
        this.userPrototype = userPrototype;
    }

    @GetMapping("/login")
    public String getLoginUser(Model model) {
        model.addAttribute("user", userPrototype);
        return "login";
    }

    @PostMapping("/login")
    public String checkUser(@ModelAttribute User user, HttpSession session, Model model) {
        if (userService.isUsernameEmpty(user.getUsername())) {
            model.addAttribute("error_username", "Порожнє ім'я");
            return "login";
        }
        if (userService.isUsernameShort(user.getUsername())) {
            model.addAttribute("error_username", "Ім'я повинно бути мати хоча б 3 символи");
            return "login";

        }
        if (userService.isUsernameHasSpace(user.getUsername())) {
            model.addAttribute("error_username", "Ім'я не повинно містити пробіли");
            return "login";
        }

        if (userService.isPasswordEmpty(user.getPassword())) {
            model.addAttribute("error_password", "Порожній пароль");
            return "login";
        }
        if (userService.isPasswordShort(user.getPassword())) {
            model.addAttribute("error_password", "Пароль повинен містити хоча б 5 символів");
            return "login";
        }
        if (userService.isPasswordHasSpace(user.getPassword())) {
            model.addAttribute("error_password", "Пароль не повинен містити пробіли");
            return "login";
        }
        if (userService.isPasswordHasLower(user.getPassword())) {
            model.addAttribute("error_password", "Пароль повинен містити 1 маленьку літеру");
            return "login";
        }
        if (userService.isPasswordHasUpper(user.getPassword())) {
            model.addAttribute("error_password", "Пароль повинен містити 1 велику літеру");
            return "login";
        }

        Optional<User> userCheck = userService.checkUser(user.getUsername(), user.getPassword());
        if (userCheck.isPresent()) {
            session.setAttribute("username", user.getUsername());
            return "redirect:/" + user.getUsername();
        } else {
            model.addAttribute("error_username", "Невірне ім'я та/або пароль");
            model.addAttribute("error_password", "Невірне ім'я та/або пароль");
            return "login";
        }
    }
}
