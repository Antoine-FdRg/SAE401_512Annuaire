package fr.seinksansdooze.backend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;

@SpringBootApplication
public class SSDBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSDBackendApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Documentation 512 backend")
                        .description("Ceci est la documentation de l'api 512, qui permet la connexion avec la base de données Active Directory."));
    }

    /**
     * Autoriser les cors pendant le development de l'application.
     */
    @Bean
    public WebFilter corsFilter() {
        return (exchange, chain) -> {
            exchange.getResponse()
                    .getHeaders()
                    .add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            exchange.getResponse()
                    .getHeaders()
                    .add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, PUT, POST, DELETE, OPTIONS");
            exchange.getResponse()
                    .getHeaders()
                    .add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
            exchange.getResponse()
                    .getHeaders()
                    .add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            return chain.filter(exchange);
        };
    }

    @Bean
    public WebFluxConfigurer corsConfigurer() {
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }
}
