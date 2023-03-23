package fr.seinksansdooze.backend.connectionManaging.rateLimit;

public enum RateLimiterSingleton {
    INSTANCE;

    private final RateLimiter rateLimiter;

    RateLimiterSingleton() {
        rateLimiter = new RateLimiter(10,60);
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }
}
