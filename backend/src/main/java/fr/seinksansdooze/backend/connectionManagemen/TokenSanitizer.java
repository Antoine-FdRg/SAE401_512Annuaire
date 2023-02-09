package fr.seinksansdooze.backend.connectionManagemen;

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
