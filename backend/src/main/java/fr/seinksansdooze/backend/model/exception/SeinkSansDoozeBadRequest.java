package fr.seinksansdooze.backend.model.exception;

import org.springframework.http.HttpStatus;

public class SeinkSansDoozeBadRequest extends SeinkSansDoozeBackException {
    public SeinkSansDoozeBadRequest() {
        super(HttpStatus.BAD_REQUEST, "Erreur lors de la formulation de la recherche.");
    }
}
