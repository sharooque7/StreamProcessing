package org.ainzson.oops.collections.queue;

import org.ainzson.Main;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class LRUCache <K,V> {

    private final int capacity;
    private final Map<K, V> cache;
    private final Deque<K> orders;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.orders = new ArrayDeque<>();
    }

    public V get(K key) {
        if(!cache.containsKey(key)) return null;

        orders.remove(key);
        orders.addLast(key);
        return cache.get(key);
    }

    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            cache.put(key,value);
            orders.remove(key);
            orders.addLast(key);
        }
        else {
            if (capacity == cache.size()) {
                K lruKey = orders.pollFirst();
                cache.remove(lruKey);
            }

            cache.put(key,value);
            orders.addLast(key);
        }
    }

    // Display cache
    public void displayCache() {
        System.out.println("Cache: " + orders);
    }

}
