package fr.seinksansdooze.backend.model.exception.structure;

import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class SeinkSansDoozeStructureNotFound extends SeinkSansDoozeBackException {
    public SeinkSansDoozeStructureNotFound() {
        super(HttpStatus.NOT_FOUND, "La structure n'existe pas.");
    }
}
