package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiterSingleton;
import fr.seinksansdooze.backend.connectionManaging.tokenManaging.TokenSanitizer;
import fr.seinksansdooze.backend.model.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller permettant de gérer les requêtes des membres connectés
 */
@RestController
@RequestMapping("/api/member")
public class MemberController {
    ADConnectionManager connectionManager = ADConnectionManagerSingleton.INSTANCE.get();

    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@CookieValue("token") String token, @RequestBody String cn, @RequestBody String oldPassword, @RequestBody String newPassword, HttpServletRequest request){
        RateLimiterSingleton.INSTANCE.get().tryConsume(request.getRemoteAddr());

        if(!new TokenSanitizer().valideToken(token)){
            throw new SeinkSansDoozeBackException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Token invalide");
        }
        try {
            connectionManager.getQuerier(token).changePassword(cn, oldPassword, newPassword);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Token introuvable");
        }
        return ResponseEntity.ok().build();
    }
}
