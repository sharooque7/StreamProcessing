package org.ainzson.oops.collections.service;

import org.ainzson.oops.model.Task;

import java.util.*;

public class TaskManager {

    private final Map<Integer, Task> taskMap = new HashMap<>();
    private final Set<Task> completedTask = new HashSet<>();
    private final List<Task> taskList = new ArrayList<>();

    public void addTask(Task task) {
        taskMap.put(task.getId(), task);
        taskList.add(task);
    }

    public void completedTask(int taskId) {
        Task task =  taskMap.remove(taskId);
        if(task != null) {
            completedTask.add(task);
        }
    }

    public void showAllTask() {
        taskList.forEach(System.out::println);
    }
}
