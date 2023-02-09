package fr.seinksansdooze.backend.connectionManagemen;

public interface ITokenSanitizer {
    /**
     * Vérifie si le token est valide
     * @param token le token à vérifier
     * @return true si le token est valide, false sinon
     */
    public boolean valideToken(String token);
}
