package org.ainzson.oops.collections.lists;

import java.util.*;

public class TaskManager {

    private List<Task> tasks = new ArrayList<>();
    private List<Task> completedTasks = new LinkedList<>();

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void completedTask(String name) {
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getName().equals(name)) {
                completedTasks.add(task);
                iterator.remove();
                System.out.println("Task completed: " + name);
                return;
            }

        }
    }

    public void viewTasks() {
        tasks.sort(Comparator.comparingInt(Task::getPriority));
        System.out.println("Current Tasks: " + tasks);

    }
    public void viewCompletedTasks() {
        System.out.println("Completed Tasks: " + completedTasks);
    }
}
