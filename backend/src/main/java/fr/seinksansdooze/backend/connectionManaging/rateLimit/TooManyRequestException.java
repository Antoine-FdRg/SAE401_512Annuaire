package fr.seinksansdooze.backend.connectionManaging.rateLimit;

import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class TooManyRequestException extends SeinkSansDoozeBackException {

    public TooManyRequestException() {
        super(HttpStatus.TOO_MANY_REQUESTS,
                "Trop de requêtes, veuillez patienter.");

    }

}
