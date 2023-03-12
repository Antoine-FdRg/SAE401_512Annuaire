package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.payload.LoginPayload;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginPayload payload) {
        // TODO: 10/03/2023 verification mdp

        // Durée de vie de la session
        int sessionAge;
        if (payload.isRememberMe()) {
            sessionAge = 24 * 60 * 60 * 30; // 1 mois
        } else {
            sessionAge = 60 * 60; // 1 heure
        }

        ResponseCookie responseCookie = ResponseCookie.from("token", "TODO")
                .httpOnly(true)
                //.secure(false)
                .path("/")
                .maxAge(sessionAge)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("Connexion avec succès");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
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
