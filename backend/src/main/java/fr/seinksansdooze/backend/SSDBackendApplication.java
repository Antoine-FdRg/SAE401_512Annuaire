package fr.seinksansdooze.backend;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
}
