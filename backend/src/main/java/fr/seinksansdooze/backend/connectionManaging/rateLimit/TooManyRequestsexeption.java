package fr.seinksansdooze.backend.connectionManaging.rateLimit;

import fr.seinksansdooze.backend.model.SeinkSansDoozeBackException;
import org.springframework.http.HttpStatus;

public class TooManyRequestsexeption extends SeinkSansDoozeBackException {

    public TooManyRequestsexeption() {
        super(HttpStatus.TOO_MANY_REQUESTS,
                "Trop de requêtes, veuillez patienter.");

    }

}
