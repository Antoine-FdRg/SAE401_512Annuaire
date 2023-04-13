package fr.seinksansdooze.backend.model.exception.group;

import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class SeinkSansDoozeGroupNotFound extends SeinkSansDoozeBackException {
    public SeinkSansDoozeGroupNotFound() {
        super(HttpStatus.NOT_FOUND, "Le groupe n'existe pas.");
    }
}
