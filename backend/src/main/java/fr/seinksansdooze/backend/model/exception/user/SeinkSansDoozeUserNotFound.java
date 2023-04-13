package fr.seinksansdooze.backend.model.exception.user;

import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class SeinkSansDoozeUserNotFound extends SeinkSansDoozeBackException {
    public SeinkSansDoozeUserNotFound() {
        super(HttpStatus.NOT_FOUND, "L'utilisateur n'existe pas.");
    }
}
