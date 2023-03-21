package fr.seinksansdooze.backend;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.IPublicADQuerier;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SSDBackendApplicationTests {
    /**
     * Nous mockons ce service ici pour eviter qu'il se connecte à AD au
     * lancement de l'application lors des tests.
     */
    @MockBean
    private IPublicADQuerier querier;

    @Test
    void contextLoads() {
    }

}
