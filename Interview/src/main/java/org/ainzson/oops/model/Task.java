package org.ainzson.oops.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Comparable<Task> {
    private int id;
    private String title;
    private String description;
    private int priority;
    private LocalDateTime deadline;

    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.priority,o.priority);
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " (Priority: " + priority + ")";
    }

}
