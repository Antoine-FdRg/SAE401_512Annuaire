package fr.seinksansdooze.backend.model.exception.user;

import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class SeinkSansDoozeUserIncomplete extends SeinkSansDoozeBackException {

    public SeinkSansDoozeUserIncomplete() {
        super(HttpStatus.NOT_IMPLEMENTED, "Les attributs de l'utilisateur sont incomplets");
    }

}
