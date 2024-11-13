package com.example.TaskScheduler.service;

import com.example.TaskScheduler.data.UserRepositoryStub;
import com.example.TaskScheduler.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Scope("singleton")
public class UserService {
    private static int userID = 0;
    private UserRepositoryStub userRepositoryStub;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserRepositoryStub userRepositoryStub) {
        this.userRepositoryStub = userRepositoryStub;
    }

    public boolean isUsernameTaken(String username) {
        return userRepositoryStub.getUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    public boolean isUsernameEmpty(String username) {
        return username.trim().isEmpty();
    }

    public boolean isUsernameShort(String username) {
        return username.trim().length() < 3;
    }

    public boolean isUsernameHasSpace(String username) {
        return username.contains(" ");
    }

    public Optional<User> checkUser(String username, String password) {
        return userRepositoryStub.getUsers().stream()
                .filter(user -> user.getUsername().equals(username)
                        && passwordEncoder.matches(password, user.getPassword()))
                .findFirst();
    }

    public boolean isPasswordEmpty(String password) {
        return password.trim().isEmpty();
    }

    public boolean isPasswordShort(String password) {
        return password.length() < 5;
    }

    public boolean isPasswordHasSpace(String password) {
        return password.contains(" ");
    }

    public boolean isPasswordHasUpper(String password) {
        return !password.matches(".*[A-ZА-ЯЁЇЄІҐ].*");
    }

    public boolean isPasswordHasLower(String password) {
        return !password.matches(".*[a-zа-яёїєіґ].*");
    }

    public void addUser(User user) {
        user.setId(++userID);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepositoryStub.getUsers().add(user);
    }
}
