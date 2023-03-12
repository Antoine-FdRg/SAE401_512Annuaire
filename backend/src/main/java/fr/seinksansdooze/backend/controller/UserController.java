package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.IAdminADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.TokenGenerator;
import fr.seinksansdooze.backend.connectionManaging.TokenSanitizer;
import fr.seinksansdooze.backend.model.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.payload.LoginPayload;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.NamingException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    ADConnectionManager connectionManager = new ADConnectionManager(
            new TokenGenerator(),
            new TokenSanitizer(),
            IAdminADQuerier.class
    );

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginPayload payload) {
        String token;

        try {
            token = connectionManager.addConnection(payload);
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

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
//        TODO connectionManager.logout...

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
