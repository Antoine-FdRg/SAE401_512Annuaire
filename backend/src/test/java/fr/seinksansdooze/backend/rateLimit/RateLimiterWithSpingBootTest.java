package fr.seinksansdooze.backend.rateLimit;

import fr.seinksansdooze.backend.connectionManaging.TimeHelper;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiter;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiterSingleton;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class RateLimiterWithSpingBootTest {

    RateLimiter rateLimiter = new RateLimiter(10, 10);

    @Value("${local.server.port}")
    int port;

    @Test
    public void testRateLimiterWithSpingBootTest() {
        // mock static method RateLimiterSingleton.INSTANCE.get()

        try (MockedStatic<RateLimiterSingleton> mockedStatic = Mockito.mockStatic(RateLimiterSingleton.class)) {
            mockedStatic.when(RateLimiterSingleton::get).thenReturn(rateLimiter);

            // make 10 requests to the route /api/v1/connexion and check that the 11th request is rejected
            // make a WebClient with the port of the server
            WebClient webClient = WebClient.create("http://localhost:"+port);
            for (int i = 0; i < 10; i++) {
                System.out.println(webClient.get().uri("/api/public/search/person").retrieve().bodyToMono(String.class).block());
            }

            // the 11th request should be rejected




        }
    }
}

