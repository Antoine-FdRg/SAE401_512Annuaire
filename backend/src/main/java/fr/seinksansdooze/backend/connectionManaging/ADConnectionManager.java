package fr.seinksansdooze.backend.connectionManaging;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.tokenManaging.ITokenGenerator;
import fr.seinksansdooze.backend.connectionManaging.tokenManaging.ITokenSanitizer;
import fr.seinksansdooze.backend.model.response.LoggedInUser;

import javax.naming.NamingException;
import java.lang.reflect.InvocationTargetException;

/**
 * Cette classe permet de gérer toutes les connexions à Active Directory.
 * Elle fera le lien entre les différentes Token et les différentes connexions
 * Le but premier est de pouvoir garder des connexions actives tant qu'un token est valide
 * Et de pouvoir les fermer une fois le token expiré et ainsi libérer la mémoire
 */
public class ADConnectionManager {
    private final ConcurrentHashMapAutoCleaning<String, ADConnection> connections = new ConcurrentHashMapAutoCleaning<>(3600000);
    private final ITokenGenerator tokenGenerator;
    private final ITokenSanitizer tokenSanitizer;
    /**
     * Le querierClass est la classe qui permet de créer un nouveau querier doit implémenter IAuthentifiedADQuerier
     */
    private final Class<?> querierClass;


    public ADConnectionManager(ITokenGenerator tokenGenerator, ITokenSanitizer tokenSanitizer, Class<?> querierClass) {
        this.tokenGenerator = tokenGenerator;
        this.tokenSanitizer = tokenSanitizer;
        this.querierClass = querierClass;
        connections.setCleanPeriod(5 * 60 * 1000);
    }

    /**
     * Cette méthode permet de créer une connexion à Active Directory
     * Elle prend en paramètre le nom d'utilisateur et le mot de passe
     * Elle retourne un token de connexion si la connexion est réussie
     * Elle retourne une exception si la connexion échoue
     *
     * @return le token de connexion
     * @throws NamingException si la connexion échoue
     */

    public Object[] addConnection(String username, String password) throws NamingException {

        //on crée un nouveau querier pour la connexion avec l'ObjectClass
        IAuthentifiedADQuerier querier;

        try {
            querier = (IAuthentifiedADQuerier) querierClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        LoggedInUser connectedUser = querier.login(username, password);
        if (connectedUser!=null) {
            String token = this.tokenGenerator.generateNewToken();
            connectedUser.setToken(token);
            ADConnection connection = new ADConnection();
            connection.setQuerier(querier);
            this.connections.put(token, connection);
            Object[] userAndToken = new Object[2];
            userAndToken[0] = connectedUser;
            userAndToken[1] = token;
            return userAndToken;
        } else {
            throw new NamingException("Connection failed");
        }
    }

    /**
     * Supprime une connexion de force.
     *
     * @param token Le token de la connexion à supprimer.
     */
    public void removeConnection(String token) {
        ADConnection connection = connections.get(token);
        if (connection == null) return;

        connection.getQuerier().logout();
        connections.remove(token);
    }

    /**
     * Cette méthode permet de vérifier si un token est valide
     * Elle retourne le Querier associé au token si le token est valide
     *
     * @param token le token à vérifier
     * @return le Querier associé au token
     * @throws NamingException si le token n'est pas valide
     */
    public IAuthentifiedADQuerier getQuerier(String token) throws NamingException {
        //sanitize token
        if (!this.tokenSanitizer.valideToken(token)) {
            throw new NamingException("Token not valid");
        }
        //check if token exists
        if (!connections.containsKey(token)) {
            throw new NamingException("Token not found");
        }
        ADConnection connection = connections.get(token);
        //check if token is expired
        if (connection.isExpired()) {
            throw new NamingException("Token expired");
        }
        //update last use date
        connection.updateLastUseDate();
        //return querier
        return connection.getQuerier();

    }

    public void close() {
        this.connections.close();
    }

}





