package fr.seinksansdooze.backend.connectionManagemen;

public interface ITokenGenerator {

    /**
     * Génère un nouveau token
     * @return le token généré
     */
    public String generateNewToken();
}
