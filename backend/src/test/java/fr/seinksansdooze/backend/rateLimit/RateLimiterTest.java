package fr.seinksansdooze.backend.rateLimit;

import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RateLimiterTest {
    @Test
    public void testTryConsumeWithExceedingRequests() throws InterruptedException {
        int maxRequests = 5;
        int timeWindow = 1;
        RateLimiter rateLimiter = new RateLimiter(maxRequests, timeWindow);

        // Attempt to consume more than the maximum number of requests
        String key = "user1";
        for (int i = 0; i < maxRequests + 1; i++) {
            boolean result = rateLimiter.tryConsume(key);
            if (i < maxRequests) {
                Assertions.assertTrue(result, "The first " + maxRequests + " requests should be allowed");
            } else {
                Assertions.assertFalse(result, "The request should be rejected after exceeding the limit");
            }
        }

        // Wait for the time window to expire
        TimeUnit.SECONDS.sleep(timeWindow);

        // Attempt to consume requests after the time window has expired
        boolean result = rateLimiter.tryConsume(key);
        Assertions.assertTrue(result, "The request should be allowed after the time window has expired");

        rateLimiter.close();
    }

    @Test
    public void testTryConsumeWithMultipleUsers() throws InterruptedException {
        int maxRequests = 1000;
        int timeWindow = 20;
        RateLimiter rateLimiter = new RateLimiter(maxRequests, timeWindow);

        int numUsers = 3000;
        int numRequestsPerUser = 1000;

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numUsers; i++) {
            String user = "user" + i;
            Thread thread = new Thread(() -> {
                for (int j = 0; j < numRequestsPerUser+1; j++) {
                    boolean result = rateLimiter.tryConsume(user);
                    if (j < maxRequests) {
                        Assertions.assertTrue(result, "The first " + maxRequests + " requests should be allowed");
                    } else {
                        Assertions.assertFalse(result, "The request should be rejected after exceeding the limit");
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        // Verify that each user has made the expected number of requests
        for (int i = 0; i < numUsers; i++) {
            String user = "user" + i;
            Assertions.assertFalse(rateLimiter.tryConsume(user),
                    "The bucket should be fully recharged for user " + user);
        }
    }
}
