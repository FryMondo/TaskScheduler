package com.example.TaskScheduler.data;

import com.example.TaskScheduler.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryStub {
    private final List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }
}
