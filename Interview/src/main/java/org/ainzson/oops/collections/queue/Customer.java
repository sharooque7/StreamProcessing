package org.ainzson.oops.collections.queue;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
public class Customer implements Comparable<Customer> {
    String name;
    int priority; // Lower value = Higher priority

    public Customer(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }
    @Override
    public int compareTo(Customer o) {
        return Integer.compare(o.priority, this.priority); // Max
    }
}
