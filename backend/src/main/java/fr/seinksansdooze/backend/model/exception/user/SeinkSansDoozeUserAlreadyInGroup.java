package fr.seinksansdooze.backend.model.exception.user;

import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class SeinkSansDoozeUserAlreadyInGroup extends SeinkSansDoozeBackException {
    public SeinkSansDoozeUserAlreadyInGroup() {
        super(HttpStatus.CONFLICT, "L'utilisateur est déjà dans le groupe.");
    }
}
