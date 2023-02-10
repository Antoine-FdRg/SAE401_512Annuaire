package fr.seinksansdooze.backend.model;

/**
 * Type qui correspond au JSON qui est envoyé lors d'une connexion (login).
 */
public class LoginPayload {
    private String username;
    private String password;

    public LoginPayload() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
