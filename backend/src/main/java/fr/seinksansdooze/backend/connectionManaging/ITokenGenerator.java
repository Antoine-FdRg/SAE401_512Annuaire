package fr.seinksansdooze.backend.connectionManaging;

public interface ITokenGenerator {

    /**
     * Génère un nouveau token
     * @return le token généré
     */
    public String generateNewToken();
}
