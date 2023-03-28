package fr.seinksansdooze.backend.rateLimit;

import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiter;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.TooManyRequestsexeption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RateLimiterTest {
    @Test
    public void testTryConsumeWithExceedingRequests() throws InterruptedException {
        int maxRequests = 5;
        int timeWindow = 1;
        RateLimiter rateLimiter = new RateLimiter(maxRequests, timeWindow);

        // Attempt to consume more than the maximum number of requests
        String key = "user1";
        for (int i = 0; i < maxRequests + 1; i++) {
            if (i < maxRequests) {
                assertDoesNotThrow(() -> rateLimiter.tryConsume(key), "The request should be allowed");
            } else {
                assertThrows(TooManyRequestsexeption.class, () -> rateLimiter.tryConsume(key), "The request should be rejected");
            }
        }

        // Wait for the time window to expire
        TimeUnit.SECONDS.sleep(timeWindow);

        // Attempt to consume requests after the time window has expired
        assertDoesNotThrow(() -> rateLimiter.tryConsume(key), "The request should be rejected");

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
                    if (j < maxRequests) {
                        assertDoesNotThrow(() -> rateLimiter.tryConsume(user), "The request should be allowed");
                    } else {
                        assertThrows(TooManyRequestsexeption.class, () -> rateLimiter.tryConsume(user), "The request should be rejected");
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
            assertThrows(TooManyRequestsexeption.class, () -> rateLimiter.tryConsume(user),
                    "The request should be rejected");
        }
    }
}
