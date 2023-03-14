package fr.seinksansdooze.backend.connectionManaging;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.ADQuerier;

public enum ADConnectionManagerSingleton {
    INSTANCE;
    public ADConnectionManager connectionManager;

    ADConnectionManagerSingleton() {
        connectionManager = new ADConnectionManager(new TokenGenerator(),new TokenSanitizer(), ADQuerier.class);
    }

    public ADConnectionManager get() {
        return connectionManager;
    }
}
