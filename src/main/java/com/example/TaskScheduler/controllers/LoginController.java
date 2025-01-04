package com.example.TaskScheduler.controllers;

import com.example.TaskScheduler.models.User;
import com.example.TaskScheduler.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class LoginController {
    private final UserService userService;
    private final User userPrototype;

    @Autowired
    public LoginController(UserService userService, User userPrototype) {
        this.userService = userService;
        this.userPrototype = userPrototype;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> getLoginUser(Model model) {
        model.addAttribute("user", userPrototype);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Object> checkUser(@ModelAttribute User user, HttpSession session, Model model) {
        if (userService.isUsernameEmpty(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.isUsernameShort(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
        if (userService.isUsernameHasSpace(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (userService.isPasswordEmpty(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.isPasswordShort(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.isPasswordHasSpace(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.isPasswordHasLower(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.isPasswordHasUpper(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<User> userCheck = userService.checkUser(user.getUsername(), user.getPassword());
        if (userCheck.isPresent()) {
            session.setAttribute("username", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            model.addAttribute("error_username", "Невірне ім'я та/або пароль");
            model.addAttribute("error_password", "Невірне ім'я та/або пароль");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
