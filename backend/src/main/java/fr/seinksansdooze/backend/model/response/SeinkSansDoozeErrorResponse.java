package fr.seinksansdooze.backend.model.response;

/**
 * Renvoyé quand une erreur se produit.
 */
public class SeinkSansDoozeErrorResponse {
    private String error;

    public SeinkSansDoozeErrorResponse() {
    }

    public SeinkSansDoozeErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
