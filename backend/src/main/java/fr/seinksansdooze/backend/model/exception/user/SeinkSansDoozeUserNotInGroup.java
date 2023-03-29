package fr.seinksansdooze.backend.model.exception.user;

import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class SeinkSansDoozeUserNotInGroup extends SeinkSansDoozeBackException {
    public SeinkSansDoozeUserNotInGroup() {
        super(HttpStatus.NOT_FOUND, "L'utilisateur n'est pas dans le groupe");
    }
}
