package fr.seinksansdooze.backend.connectionManaging;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;

import java.util.Date;

/**
 * Cette classe permet de gérer une connexion à Active Directory
 * Elle possèdera une date d'émission de token et la dernière utilisation
 */
public class ADConnection {
    private Date tokenEmissionDate;
    private Date lastUseDate; //TODO ne pas couper la connexion si elle a été utilisée il a peu de temps
    private IAuthentifiedADQuerier querier;
    /**
     * Temps d'expiration d'une connexion en millisecondes
     */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public ADConnection() {
        setTokenEmissionDate(new Date());
        this.lastUseDate = new Date();
    }

    public Date getTokenEmissionDate() {
        return tokenEmissionDate;
    }

    public Date getLastUseDate() {
        return lastUseDate;
    }

    public IAuthentifiedADQuerier getQuerier() {
        return querier;
    }

    private void setTokenEmissionDate(Date tokenEmissionDate) {
        this.tokenEmissionDate = tokenEmissionDate;
    }

    public void setQuerier(IAuthentifiedADQuerier querier) {
        this.querier = querier;
    }

    public boolean isExpired() {
        //On vérifie si la date d'émission du token est antérieure à la date actuelle moins le temps d'expiration
        boolean isExpired = this.tokenEmissionDate.before(new Date(new Date().getTime() - EXPIRATION_TIME));
        //TODO: On vérifie si la date de dernière utilisation est antérieure à la date actuelle moins le temps d'expiration
        return isExpired;
    }

    public void updateLastUseDate() {
        this.lastUseDate = new Date();
    }
}


