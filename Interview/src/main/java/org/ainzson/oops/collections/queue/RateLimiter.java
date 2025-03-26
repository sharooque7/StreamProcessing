package org.ainzson.oops.collections.queue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RateLimiter {
    private final int maxRequests;
    private final long timeWindow;
    private final Map<String, Queue<Long>> userRequests;

    public RateLimiter(int maxRequests, long timeWindowInMillis) {
        this.maxRequests = maxRequests;
        this.timeWindow = timeWindowInMillis;
        this.userRequests = new HashMap<>();
    }

    public synchronized boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();
        userRequests.putIfAbsent(userId, new LinkedList<>());
        Queue<Long> requestQueue = userRequests.get(userId);

        // Remove requests older than the time window
        while (!requestQueue.isEmpty() && (currentTime - requestQueue.peek() > timeWindow)) {
            requestQueue.poll();
        }

        // Check if the user has exceeded the limit
        if (requestQueue.size() < maxRequests) {
            requestQueue.add(currentTime);
            return true; // Request allowed
        }
        return false; // Request denied
    }
}
