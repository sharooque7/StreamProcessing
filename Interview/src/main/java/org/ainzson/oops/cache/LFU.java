package org.ainzson.oops.cache;

import java.security.Key;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFU<K,V> {

    private final int capacity;
    private int minFreq;
    private final Map<K, V> cache;
    private final Map<K, Integer> freeMap;
    private final Map<Integer, LinkedHashSet<K>> freqListMap;


    public LFU(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.cache = new HashMap<>();
        this.freeMap = new HashMap<>();
        this.freqListMap = new HashMap<>();
    }

    public V get(K key) {
        if (!cache.containsKey(key)) return null;

        int freq = freeMap.get(key);
        freeMap.put(key, freq + 1);

        freqListMap.get(freq).remove(key);

        if(freqListMap.get(freq).isEmpty()) {
            freqListMap.remove(freq);
            if(freq == minFreq) minFreq++;
        }

        freqListMap.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);

        return cache.get(key);

    }

    public void put(K key, V value) {
        if (capacity == 0) return;

        if (cache.containsKey(key)) {
            cache.put(key, value);
            get(key);
            return;
        }


        if (cache.size() >= capacity) {
            LinkedHashSet<K> lfukeys = freqListMap.get(key);
            K lfukey = lfukeys.iterator().next();
            lfukeys.remove(lfukey);
            if (lfukeys.isEmpty()) {
                freqListMap.remove(minFreq);
            }
            cache.remove(lfukey);
            freeMap.remove(lfukey);
        }

        cache.put(key, value);
        freeMap.put(key,1);
        freqListMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq =  1;
    }
}
