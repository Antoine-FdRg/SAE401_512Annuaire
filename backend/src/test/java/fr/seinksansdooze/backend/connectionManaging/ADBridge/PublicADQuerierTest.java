package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.model.ObjectType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.SearchResult;
import java.util.Iterator;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicADQuerierTest {

    @Mock
    PublicADQuerier publicADQuerier;

    @Mock
    Attributes mockedAttributes;

    @BeforeEach
    void setUp() {
        when(mockedAttributes.get(anyString())).thenReturn(new BasicAttribute("mocked", "OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));
    }

    private NamingEnumeration<SearchResult> generateDummyNamingEnumeration(int size) {
        return new NamingEnumeration<>() {
            private final Iterator<Integer> iterator = IntStream.range(0, size).iterator();

            private SearchResult myNext() {
                Integer value = iterator.next();
                return new SearchResult(value.toString(), value, mockedAttributes);
            }

            @Override
            public SearchResult next() {
                return myNext();
            }

            @Override
            public boolean hasMore() {
                return iterator.hasNext();
            }

            @Override
            public void close() {
            }

            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public SearchResult nextElement() {
                return myNext();
            }
        };
    }

    @Test
    void testPagination() {
        when(publicADQuerier.search(eq(ObjectType.PERSON), eq("5"))).thenReturn(generateDummyNamingEnumeration(5));
        when(publicADQuerier.search(eq(ObjectType.PERSON), eq("10"))).thenReturn(generateDummyNamingEnumeration(50));
        when(publicADQuerier.search(eq(ObjectType.PERSON), eq("14"))).thenReturn(generateDummyNamingEnumeration(3));
        when(publicADQuerier.search(eq(ObjectType.PERSON), eq("7"))).thenReturn(generateDummyNamingEnumeration(0));

        when(publicADQuerier.searchPerson(anyString(), anyInt(), anyInt())).thenCallRealMethod();

        assertEquals(5, publicADQuerier.searchPerson("5", 0, 15).size());
        assertEquals(10, publicADQuerier.searchPerson("10", 1, 10).size());
        assertEquals(0, publicADQuerier.searchPerson("14", 20, 15).size());
        assertEquals(0, publicADQuerier.searchPerson("7", 48, 2).size());
    }
}