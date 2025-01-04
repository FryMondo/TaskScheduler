package com.example.TaskScheduler.data;

import com.example.TaskScheduler.models.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TaskRepositoryStub {
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getTasksWithPagination(int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, tasks.size());
        if (start > end) {
            return Collections.emptyList();
        }
        return tasks.subList(start, end);
    }


}
