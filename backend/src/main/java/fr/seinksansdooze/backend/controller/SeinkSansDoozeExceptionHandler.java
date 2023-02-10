package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.response.SeinkSansDoozeErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SeinkSansDoozeExceptionHandler {
    /**
     * Gère les {@link SeinkSansDoozeBackException}.
     */
    @ExceptionHandler(SeinkSansDoozeBackException.class)
    public ResponseEntity<SeinkSansDoozeErrorResponse> errorHandler(SeinkSansDoozeBackException exception) {
        return new ResponseEntity<>(
                new SeinkSansDoozeErrorResponse(exception.getMessage()),
                exception.getStatus()
        );
    }
}
