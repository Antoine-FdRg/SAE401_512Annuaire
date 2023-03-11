package fr.seinksansdooze.backend.connectionManaging;


import fr.seinksansdooze.backend.connectionManaging.ADBridge.IAdminADQuerier;
import fr.seinksansdooze.backend.model.payload.LoginPayload;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ADConexionManagerTest {

    @Test
    public void test() {

        ADConnectionManager manager = new ADConnectionManager(new TokenGenerator(), new TokenSanitizer(), ADQuerierMoke.class);
        AtomicReference<String> token = new AtomicReference<>();
        assertDoesNotThrow(() -> {
            token.set(manager.addConnection(new LoginPayload("user1", "mdp1",false)));
        });
        assertThrows(NamingException.class, () -> manager.addConnection(new LoginPayload("user2", "mdp2",false)));
        assertDoesNotThrow(() -> {
            IAdminADQuerier q= manager.getQuerier(token.get());
            assertTrue(q instanceof ADQuerierMoke);
        });
        manager.close();
    }
}
