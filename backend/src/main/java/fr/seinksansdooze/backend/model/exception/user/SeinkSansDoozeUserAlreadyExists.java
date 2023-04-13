package fr.seinksansdooze.backend.model.exception.user;

import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class SeinkSansDoozeUserAlreadyExists extends SeinkSansDoozeBackException {
    public SeinkSansDoozeUserAlreadyExists() {
        super(HttpStatus.CONFLICT, "L'utilisateur existe déjà.");
    }
}
