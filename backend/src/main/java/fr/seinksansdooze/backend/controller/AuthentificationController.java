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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * Controller permettant de gérer les requêtes d'authentification et de déconnexion
 */
@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthentificationController {

    @Value("${session.duration.long}")
    private int longSessionDuration;

    @Value("${session.duration.short}")
    private int shortSessionDuration;

    ADConnectionManager connectionManager = ADConnectionManagerSingleton.INSTANCE.get();

    //TODO comprendre pq les requete public ne fontyionne plus apres une connexion

    @Operation(summary = "Connecte un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête malformée, vérifiez que le payload contient bien les champs demandé"),
            @ApiResponse(responseCode = "401", description = "Identifiant ou mot de passe incorrect")
    })
    @PostMapping("/login")
    public ResponseEntity<LoggedInUser> login(@Valid @RequestBody LoginPayload payload, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()),1);

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

        // Durée de vie de la session
        int sessionAge;
        if (payload.isRememberMe()) {
            sessionAge = longSessionDuration;
        } else {
            sessionAge = shortSessionDuration;
        }

        ResponseCookie responseCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                //.secure(false) // a voir ce qu'on fait (le cookie est seulement transmis par https
                .path("/")
                .maxAge(sessionAge)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(connectedUser);
    }

    @Operation(summary = "Déconnecte un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Déconnexion faite avec succès : la session n'existe plus")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue("token") String token, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));

        connectionManager.removeConnection(token);

        ResponseCookie resCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(-1)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body("Déconnexion avec succès");
    }
}
