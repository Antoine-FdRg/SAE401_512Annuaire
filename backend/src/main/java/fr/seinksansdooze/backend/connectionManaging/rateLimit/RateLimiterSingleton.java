package fr.seinksansdooze.backend.connectionManaging.rateLimit;

public enum RateLimiterSingleton {
    INSTANCE;

    private final RateLimiter rateLimiter;

    RateLimiterSingleton() {
        rateLimiter = new RateLimiter(60*10,60*10);
    }

    public RateLimiter get() {
        return rateLimiter;
    }
}
