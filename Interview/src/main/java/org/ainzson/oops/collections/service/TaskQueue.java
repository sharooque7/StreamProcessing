package org.ainzson.oops.collections.service;

import org.ainzson.oops.model.Task;

import java.util.Deque;
import java.util.PriorityQueue;
import java.util.Queue;

public class TaskQueue {

    private final PriorityQueue<Task> taskQueue =  new PriorityQueue<>();

    public void addTask(Task task) {
        taskQueue.offer(task);
    }

    public Task processTask() {
        return taskQueue.poll();
    }

}
