package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiterSingleton;
import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.payload.LoginPayload;
import fr.seinksansdooze.backend.model.response.LoggedInUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;

/**
 * Controller permettant de gérer les requêtes d'authentification et de déconnexion
 */
@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {

    ADConnectionManager connectionManager = ADConnectionManagerSingleton.INSTANCE.get();

    @Operation(summary = "Connecte un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête malformée, vérifiez que le payload contient bien" +
                    " les champs demandé"),
            @ApiResponse(responseCode = "401", description = "Identifiant ou mot de passe incorrect")
    })
    @PostMapping("/login")
    public ResponseEntity<LoggedInUser> login(@Valid @RequestBody LoginPayload payload, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()), 1);

        Object[] userAndToken;

        try {
            userAndToken = connectionManager.addConnection(payload.getUsername(), payload.getPassword());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "La connexion à échouée. Veuillez verifier votre identifiant ainsi que votre mot de passe."
            );
        }

        String token = (String) userAndToken[1];
        LoggedInUser connectedUser = (LoggedInUser) userAndToken[0];

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(connectedUser);
    }

    @Operation(summary = "Déconnecte un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Déconnexion faite avec succès : la session n'existe plus")
    })
    @PostMapping("/logout")
    public String logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));

        connectionManager.removeConnection(token);

        return "Déconnexion avec succès";
    }

    @Operation(summary = "Renvoie le token passé en autorisation")
    @GetMapping("/test")
    public String getConnectedUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return "La valeur reçue : " + token;
    }
}
