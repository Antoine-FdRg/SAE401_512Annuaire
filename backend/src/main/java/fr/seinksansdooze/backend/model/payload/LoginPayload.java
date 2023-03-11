package fr.seinksansdooze.backend.model.payload;

/**
 * Type qui correspond au JSON qui est envoyé lors d'une connexion (login).
 */
public class LoginPayload {
    private String username;
    private String password;
    private boolean rememberMe = false;

    public LoginPayload() {
    }

    public LoginPayload(String username, String password, boolean rememberMe) {
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
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

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
