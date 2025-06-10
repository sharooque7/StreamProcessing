package org.ainzson.oops.cache;

import java.security.Key;
import java.util.*;

public class LRU<K, V> {
    private final int capacity;

    private final Map<K, V> cache ;

    private final Deque<K> queue;

    public LRU(int capacity) {
        this.capacity = capacity;
        this.cache =  new HashMap<>();
        this.queue = new ArrayDeque<>(capacity);
    }

    public V get(K key) {
        if (!cache.containsKey(key)) return  null;

        queue.remove();
        queue.addLast(key);
        return cache.get(key);
    }

    public void put(K key, V value) {
        if (! cache.containsKey(key)) {
            cache.put(key, value);
            queue.remove();
            queue.addLast(key);
        }
        else {
            if (capacity == queue.size()) {
                K lruKey = queue.pollFirst();
                cache.remove(lruKey);
            }

            queue.addLast(key);
            cache.put(key, value);
        }
    }
}