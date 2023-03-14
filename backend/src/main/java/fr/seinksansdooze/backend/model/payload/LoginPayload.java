package fr.seinksansdooze.backend.model.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Type qui correspond au JSON qui est envoyé lors d'une connexion (login).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPayload {
    @NotBlank
    @NotNull
    private String username;
    @NotBlank
    @NotNull
    private String password;
    private boolean rememberMe;
}
