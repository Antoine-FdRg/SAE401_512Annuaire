package fr.seinksansdooze.backend.connectionManaging.tokenManaging;

public interface ITokenGenerator {

    /**
     * Génère un nouveau token
     * @return le token généré
     */
    public String generateNewToken();
}
