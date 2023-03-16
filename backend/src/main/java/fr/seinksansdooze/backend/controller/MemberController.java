package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller permettant de gérer les requêtes des membres connectés
 */
@RestController
@RequestMapping("/api/member")
public class MemberController {
    ADConnectionManager connectionManager = ADConnectionManagerSingleton.INSTANCE.get();


}
