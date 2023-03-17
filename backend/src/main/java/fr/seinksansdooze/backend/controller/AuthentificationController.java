package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import fr.seinksansdooze.backend.model.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.payload.LoginPayload;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;

/**
 * Controller permettant de gérer les requêtes d'authentification et de déconnexion
 */
@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {
    ADConnectionManager connectionManager = ADConnectionManagerSingleton.INSTANCE.get();

    //TODO comprendre pq les requete public ne fontyionne plus apres une connexion

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête malformée, vérifiez que le payload contient bien les champs demandé"),
            @ApiResponse(responseCode = "401", description = "Identifiant ou mot de passe incorrect")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginPayload payload) {
        String token;

        try {
            token = connectionManager.addConnection(payload.getUsername(), payload.getPassword());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "La connexion à échouée. Veuillez verifier votre identifiant ainsi que votre mot de passe."
            );
        }


        // Durée de vie de la session
        int sessionAge;
        if (payload.isRememberMe()) {
            sessionAge = 24 * 60 * 60 * 30; // 1 mois
        } else {
            sessionAge = 60 * 60; // 1 heure
        }

        ResponseCookie responseCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                //.secure(false) // a voir ce qu'on fait (le cookie est seulement transmis par https
                .path("/")
                .maxAge(sessionAge)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("Connexion avec succès");
    }

    /*
    POST http://localhost:8080/api/auth/logout
Content-Type: application/json
Cookie: token=9OxN39RiB9Ikfda9u78ePlNrZ43IRE5i
     */
    @ApiResponse(responseCode = "200", description = "Déconnexion faite avec succès")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue("token") String token) {
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
