package fr.seinksansdooze.backend.connectionManaging;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.IAdminADQuerier;

import javax.naming.NamingException;

import java.util.HashMap;

/**
 * Cette classe permet de gérer toutes les connexions à Active Directory
 * Elle fera le lien entre les différentes Token et les différentes connexions
 * Le but premier est de pouvoir garder des connexions actives tant qu'un token est valide
 * Et de pouvoir les fermer une fois le token expiré et ainsi libérer la mémoire
 */
public class ADConnectionManager {
    private final ConcurrentHashMapAutoCleaning<String, ADConnection> connections = new ConcurrentHashMapAutoCleaning<String, ADConnection>(3600000);
    private final ITokenGenerator tokenGenerator;
    private final ITokenSanitizer tokenSanitizer;
    /**
     * Le querierClass est la classe qui permet de créer un nouveau querier doit implémenter IADQuerier
     */
    private final Class<?> querierClass;


    public ADConnectionManager(ITokenGenerator tokenGenerator,ITokenSanitizer tokenSanitizer,Class<?> querierClass) {
        this.tokenGenerator = tokenGenerator;
        this.tokenSanitizer = tokenSanitizer;
        this.querierClass = querierClass;
    }

    /**
     * Cette méthode permet de créer une connexion à Active Directory
     * Elle prend en paramètre le nom d'utilisateur et le mot de passe
     * Elle retourne un token de connexion si la connexion est réussie
     * Elle retourne une exception si la connexion échoue
     * @param username le nom d'utilisateur
     * @param pwd le mot de passe
     * @return le token de connexion
     * @throws NamingException si la connexion échoue
     */

    public String addConnection(String username, String pwd)throws NamingException {

        //on crée un nouveau querier pour la connexion avec l'ObjectClass
        IAdminADQuerier querier;
        try {
            querier = (IAdminADQuerier)querierClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //TODO esayer de trouver une solution plus simple est non dépréciée

        boolean connectionSuccess= querier.login(username, pwd);
        if(connectionSuccess){
            String token = this.tokenGenerator.generateNewToken();
            ADConnection connection = new ADConnection();
            connection.setQuerier(querier);
            this.connections.put(token, connection);
            return token;
        }
        else{
            throw new NamingException("Connection failed");
        }
    }

    /**
     * Cette méthode permet de vérifier si un token est valide
     * Elle retourne le Querier associé au token si le token est valide
     * @param token le token à vérifier
     * @return le Querier associé au token
     * @throws NamingException si le token n'est pas valide
     */
    public IAdminADQuerier getQuerier(String token)throws NamingException {
        //sanitize token
        if (!this.tokenSanitizer.valideToken(token)){
            throw new NamingException("Token not valid");
        }
        //check if token exists
        if(!connections.containsKey(token)){
            throw new NamingException("Token not found");
        }
        ADConnection connection = connections.get(token);
        //check if token is expired
        if(connection.isExpired()){
            throw new NamingException("Token expired");
        }
        //update last use date
        connection.updateLastUseDate();
        //return querier
        return connection.getQuerier();

    }

}





