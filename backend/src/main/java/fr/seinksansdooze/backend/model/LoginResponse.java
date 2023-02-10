package fr.seinksansdooze.backend.model;

/**
 * Cet objet est renvoyé suite à un login effectué avec succès.
 */
public class LoginResponse {
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
