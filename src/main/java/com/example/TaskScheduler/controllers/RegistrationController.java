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

@RestController
public class RegistrationController {
    private final UserService userService;
    private final User userPrototype;

    @Autowired
    public RegistrationController(UserService userService, User userPrototype) {
        this.userService = userService;
        this.userPrototype = userPrototype;
    }

    @GetMapping("/registration")
    public ResponseEntity<Void> getRegisteredUser(Model model) {
        model.addAttribute("user", userPrototype);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/registration")
    public ResponseEntity<User> addUser(@ModelAttribute User user, HttpSession session, Model model) {
        if (userService.isUsernameEmpty(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.isUsernameShort(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
        if (userService.isUsernameHasSpace(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (userService.isUsernameTaken(user.getUsername())) {
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
        userService.addUser(user);
        return ResponseEntity.ok(user);
    }
}
