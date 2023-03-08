package fr.seinksansdooze.backend.model;

import fr.seinksansdooze.backend.controller.SeinkSansDoozeExceptionHandler;
import org.springframework.http.HttpStatus;

/**
 * Déclenchée pour notifier qu'une erreur lors de l'utilisation d'une route.
 * Cette exception doit obligatoirement être accompagnée d'un code d'erreur HTTP,
 * sinon elle ne pourra pas être instanciée.
 * Toutes ces erreurs lancées pendant le traitement d'une route api serons traitées dans
 * le gestionnaire d'erreur suivant : {@link SeinkSansDoozeExceptionHandler}.
 */
public class SeinkSansDoozeBackException extends RuntimeException {
    private HttpStatus status;

    public SeinkSansDoozeBackException(HttpStatus status, String errorMessage) {
        super(errorMessage);
        this.status = status;
    }

    public SeinkSansDoozeBackException(HttpStatus status, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.status = status;
    }

    private SeinkSansDoozeBackException() {
    }

    private SeinkSansDoozeBackException(String message) {
    }

    private SeinkSansDoozeBackException(String message, Throwable cause) {
        super(message, cause);
    }

    private SeinkSansDoozeBackException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
