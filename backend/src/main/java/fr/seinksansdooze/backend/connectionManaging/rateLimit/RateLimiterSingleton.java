package fr.seinksansdooze.backend.connectionManaging.rateLimit;

public enum RateLimiterSingleton {
    INSTANCE;
    private static final RateLimiter rateLimiter = new RateLimiter(100000,60*10);

    public static RateLimiter get() {
        return rateLimiter;
    }
}
