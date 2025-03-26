package org.ainzson.oops.collections.lists;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class Task {

    private String name;
    private int priority; // Lower value = Higher priority

    public Task(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return priority == task.priority && Objects.equals(name, task.name);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority);
    }
}
