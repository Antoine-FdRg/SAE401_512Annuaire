package fr.seinksansdooze.backend.connectionManaging.tokenManaging;

public class TokenSanitizer implements ITokenSanitizer{


    public boolean valideToken(String token) {
        if (token == null) {
            return false;
        }
        if (token.length() != 32) {
            return false;
        }
        return true;
    }
}
