package fr.seinksansdooze.backend.connectionManaging;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentHashmapAutoCleaningTest {
    @Test
    void testEquals() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");
        mapToTest.put("key2", "value2");
        mapRef.put("key2", "value2");
        mapToTest.put("key3", "value3");
        mapRef.put("key3", "value3");
        mapToTest.put("key4", "value4");
        mapRef.put("key4", "value4");
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        assertEquals(mapToTest, mapRef);
        // On ajoute une valeur en trop dans la map a tester
        mapToTest.put("key5", "value5");
        // On test que les 2 maps ne sont plus egales
        assertNotEquals(mapRef, mapToTest);
        assertNotEquals(mapToTest, mapRef);
        mapToTest.close();
    }

    @Test
    void testEqualsWhiveExpiredEntry() {
        //on cree une map avec une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24*60*60*1000);
        HashMap<String, String> mapRef = new HashMap<>();

        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertNotEquals(mapRef, mapToTest);
            assertNotEquals(mapToTest, mapRef);
            // On ajoute une valeur en trop dans la map a tester
            mapToTest.put("key1", "value1");
            // On test que les 2 maps ne sont plus egales
            assertEquals(mapRef, mapToTest);
            assertEquals(mapToTest, mapRef);
        }
        mapToTest.close();
    }

    @Test
    void testPut() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        // vue que la map est vide, on test que la valeur retournee est null
        assertNull(mapToTest.put("key1", "value1"));
        assertNull(mapRef.put("key1", "value1"));
        assertEquals(mapRef, mapToTest);
        // On renplace la valeur pare une nouvel on verifie que l'on recupere l'encien
        assertEquals("value1", mapToTest.put("key1", "value2"));
        assertEquals("value1", mapRef.put("key1", "value2"));
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
    }

    @Test
    void testPutWhiveExpiredEntry() {
        //on cree une map avec une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24*60*60*1000);
        HashMap<String, String> mapRef = new HashMap<>();

        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            // On ajoute des valeurs dans les 2 maps
            assertNotEquals(mapRef, mapToTest);
            assertNull(mapToTest.put("key1", "value1")); // on verifis que la valeur retournee est null mais si dans la
            // map elle est toujours presente mais plus valide
            assertEquals("value1", mapRef.put("key1", "value1"));
            assertEquals(mapRef, mapToTest);
        }
        mapToTest.close();
    }

    @Test
    void testGet() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");

        // On test le get sur une clef qui existe
        assertEquals("value1", mapToTest.get("key1"));
        assertEquals("value1", mapRef.get("key1"));
        // On test le get sur une clef qui n'existe pas
        assertNull(mapToTest.get("key2"));
        assertNull(mapRef.get("key2"));

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime - 1);
            assertEquals("value1", mapToTest.get("key1"));
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertNull(mapToTest.get("key1"));
        }
        mapToTest.close();
    }

    @Test
    void testContainsKey() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");

        // On test le containsKey sur une clef qui existe
        assertTrue(mapToTest.containsKey("key1"));
        assertTrue(mapRef.containsKey("key1"));
        // On test le containsKey sur une clef qui n'existe pas
        assertFalse(mapToTest.containsKey("key2"));
        assertFalse(mapRef.containsKey("key2"));


        mapToTest.close();
    }
    @Test
    void testContainsKeyWhiveExpiredEntry() {
        //on cree une map avec une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24*60*60*1000);

        mapToTest.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertFalse(mapToTest.containsKey("key1"));
        }
        mapToTest.close();
    }

    @Test
    void testSize() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");
        assertEquals(1, mapToTest.size());
        assertEquals(1, mapRef.size());
        mapToTest.put("key2", "value2");
        mapRef.put("key2", "value2");
        assertEquals(2, mapToTest.size());
        assertEquals(2, mapRef.size());
        mapToTest.put("key3", "value3");
        mapRef.put("key3", "value3");
        assertEquals(3, mapToTest.size());
        assertEquals(3, mapRef.size());
        mapToTest.close();
    }
    @Test
    void testSizeWhiveExpiredEntry() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertEquals(0, mapToTest.size());
        }
        mapToTest.close();
    }

    @Test
    void testIsEmpty() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        assertTrue(mapToTest.isEmpty());
        assertTrue(mapRef.isEmpty());
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");
        assertFalse(mapToTest.isEmpty());
        assertFalse(mapRef.isEmpty());
        mapToTest.close();
    }

    @Test
    void testIsEmptyWhiveExpiredEntry() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertTrue(mapToTest.isEmpty());
        }
        mapToTest.close();
    }

    @Test
    void testRemove() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");
        // On test le remove sur une clef qui existe
        assertEquals("value1", mapToTest.remove("key1"));
        assertEquals("value1", mapRef.remove("key1"));
        // On test le remove sur une clef qui n'existe pas
        assertNull(mapToTest.remove("key2"));
        assertNull(mapRef.remove("key2"));
        mapToTest.close();
    }

    @Test
    void testRemoveWhiveExpiredEntry() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertNull(mapToTest.remove("key1"));
        }
        mapToTest.close();
    }

    @Test
    void testPutAll() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");
        mapToTest.put("key2", "value2");
        mapRef.put("key2", "value2");
        mapToTest.put("key3", "value3");
        mapRef.put("key3", "value3");
        // On cree une map avec des valeurs a ajouter
        Map<String, String> mapToAdd = new HashMap<>();
        mapToAdd.put("key4", "value4");
        mapToAdd.put("key5", "value5");
        // On ajoute les valeurs a la map de reference
        mapRef.putAll(mapToAdd);
        mapToTest.putAll(mapToAdd);
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        mapToTest.close();
    }

    @Test
    void testPutAllWhiveExpiredEntry() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);

            // On cree une map avec des valeurs a ajouter
            Map<String, String> mapToAdd = new HashMap<>();
            mapToAdd.put("key2", "value2");
            // On ajoute les valeurs a la map de reference
            mapRef.putAll(mapToAdd);
            mapToTest.putAll(mapToAdd);
            assertEquals(mapToAdd, mapToTest);
            // On test que les 2 maps ne sont pas egales
            assertNotEquals(mapRef, mapToTest);
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime + 24 * 60 * 60 * 1000-1);
            // les clefs ajouter avec putAll ne sont pas supprimer
            assertEquals(mapToAdd, mapToTest);
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime + 24 * 60 * 60 * 1000);
            // les clefs ajouter avec putAll sont supprimer
            assertTrue(mapToTest.isEmpty());
        }
        mapToTest.close();
    }

    @Test
    void testcClear() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");
        mapToTest.put("key2", "value2");
        mapRef.put("key2", "value2");
        mapToTest.put("key3", "value3");
        mapRef.put("key3", "value3");
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        // On vide les 2 maps
        mapToTest.clear();
        mapRef.clear();
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        mapToTest.close();
    }

    @Test
    void testKeySet() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        for (int i = 0; i < 1000; i++) {
            mapToTest.put("key" + i, "value" + i);
            mapRef.put("key" + i, "value" + i);
        }
        // On test que les 2 maps sont egales
        Set<String> keySetRef = mapRef.keySet();
        Set<String> keySetToTest = mapToTest.keySet();
        assertTrue(keySetRef.containsAll(keySetToTest));
        assertTrue(keySetToTest.containsAll(keySetRef));
        assertEquals(keySetRef, keySetToTest);
        mapToTest.close();


        // on utilise une duree de vie de 24h
        mapToTest = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        mapToTest.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime - 1);
            assertTrue(mapToTest.keySet().contains("key1"));
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertFalse(mapToTest.keySet().contains("key1"));
        }
        mapToTest.close();



    }

    @Test
    void testValues() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        for (int i = 0; i < 1000; i++) {
            mapToTest.put("key" + i, "value" + i);
            mapRef.put("key" + i, "value" + i);
        }
        // On test que les 2 maps sont egales
        assertTrue(mapRef.values().containsAll(mapToTest.values()));
        assertTrue(mapToTest.values().containsAll(mapRef.values()));
        mapToTest.close();
    }

    @Test
    void testEntrySet() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        for (int i = 0; i < 10; i++) {
            mapToTest.put("key" + i, "value" + i);
            mapRef.put("key" + i, "value" + i);
        }
        // On test que les 2 maps sont egales

        Map.Entry<String, String> entry = mapRef.entrySet().iterator().next();
        Map.Entry<String, String> entry1;
        Iterator<Map.Entry<String, String>> it = mapToTest.entrySet().iterator();
        do {
            entry1 = it.next();
        } while (!entry1.equals(entry) && it.hasNext());
        assertEquals(entry, entry1);
        assertEquals(entry.hashCode(), entry1.hashCode());

        assertTrue(mapRef.entrySet().containsAll(mapToTest.entrySet()));
        assertTrue(mapToTest.entrySet().containsAll(mapRef.entrySet()));

        entry1.setValue("value20");
        assertEquals("value20", mapToTest.get(entry1.getKey()));
        entry.setValue("value20");
        assertEquals("value20", mapRef.get(entry.getKey()));

        mapToTest.close();
    }

    @Test
    void testPutIfAbsent() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        for (int i = 0; i < 10; i++) {
            mapToTest.put("key" + i, "value" + i);
            mapRef.put("key" + i, "value" + i);
        }
        // On ajoute une valeur a la map de reference
        assertNull(mapRef.putIfAbsent("key11", "value11"));
        // On ajoute une valeur a la map a tester
        assertNull(mapToTest.putIfAbsent("key11", "value11"));
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        // On ajoute une valeur a la map de reference
        assertEquals("value11", mapRef.putIfAbsent("key11", "value12"));
        // On ajoute une valeur a la map a tester
        assertEquals("value11", mapToTest.putIfAbsent("key11", "value12"));
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        mapToTest.close();
    }

    @Test
    void testReplace() {
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new HashMap<>();
        // On ajoute des valeurs dans les 2 maps
        for (int i = 0; i < 10; i++) {
            mapToTest.put("key" + i, "value" + i);
            mapRef.put("key" + i, "value" + i);
        }
        // On ajoute une valeur a la map de reference
        assertNull(mapRef.replace("key10", "value10"));
        // On ajoute une valeur a la map a tester
        assertNull(mapToTest.replace("key10", "value10"));
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        // On ajoute une valeur a la map de reference
        assertEquals("value9", mapRef.replace("key9", "value10"));
        // On ajoute une valeur a la map a tester
        assertEquals("value9", mapToTest.replace("key9", "value10"));
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        // On ajoute une valeur a la map de reference
        assertFalse(mapRef.replace("key1", "value9", "value11"));
        // On ajoute une valeur a la map a tester
        assertFalse(mapToTest.replace("key1", "value9", "value10"));
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        // On ajoute une valeur a la map de reference
        assertTrue(mapRef.replace("key1", "value1", "value11"));
        // On ajoute une valeur a la map a tester
        assertTrue(mapToTest.replace("key1", "value1", "value11"));
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        mapToTest.close();
    }
    @Test
    void testReplaceWhiveExpiredEntry() {
        //on cree une map avec une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24*60*60*1000);
        HashMap<String, String> mapRef = new HashMap<>();

        mapToTest.put("key1", "value1");
        mapRef.put("key1", "value1");

        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertNull(mapToTest.replace("key1", "value2"));
            assertEquals("value1", mapRef.replace("key1", "value1"));
            assertEquals(0, mapToTest.size());
            assertFalse(mapToTest.replace("key1", "value1", "value2"));
            assertTrue(mapRef.replace("key1", "value1", "value2"));
            assertEquals(0, mapToTest.size());
        }
    }

    @Test
    void testConcurrent() {
        int nbThreads = 10;
        int nbIterations = 10;
        // On cree 2 maps, une avec la classe a tester et une avec une map de reference
        // On ne test pas la supretion donc on utilise MAX_VALUE
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        Map<String, String> mapRef = new ConcurrentHashMap<>();
        // On cree un grand nombre de threads qui vont ajouter des valeurs dans les 2 maps
        ArrayList<Thread> threads = new ArrayList<>(2000);
        class treadRunner implements Runnable {
            private final Map<String, String> mapT;
            private final int threadNumberT;

            treadRunner(Map<String, String> map, int threadNumber) {
                this.mapT = map;
                this.threadNumberT = threadNumber;
            }

            @Override
            public void run() {
                for (int i = 0; i < nbIterations; i++) {
                    mapT.put("threadNumberT : " + threadNumberT + "key" + i, "value" + i);
                }
            }
        }


        for (int i = 0; i < nbThreads; i++) {
            threads.add(new Thread(new treadRunner(mapRef, i)));
            threads.get(i * 2).start();
            threads.add(new Thread(new treadRunner(mapToTest, i)));
            threads.get(i * 2 + 1).start();

        }
        // On attend que les 2 maps soient remplies
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // On test que les 2 maps sont egales
        assertEquals(mapRef, mapToTest);
        mapToTest.close();
    }

    @Test
    void testDureeDeVie() {
        // on utilise une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> mapToTest = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        // On ajoute une valeur a la map a tester
        mapToTest.put("key1", "value1");
        // On verifie que la map contient bien la valeur
        assertTrue(mapToTest.containsKey("key1"));
        // On mok System.currentTimeMillis() pour simuler le temps
        long entryCreationTime = mapToTest.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime - 1);
            assertTrue(mapToTest.containsKey("key1"));
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertFalse(mapToTest.containsKey("key1"));
        }
        mapToTest.close();
    }

    @Test
    void testDureeDeVieApresLaDerniereUtilisation() {
        // on utilise une duree de vie de 24h et une duree de vie apres la derniere utilisation de 1h
        ConcurrentHashMapAutoCleaning<String, String> mapToTestWivoutCall = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000, 60 * 60 * 1000); //todo corect
        ConcurrentHashMapAutoCleaning<String, String> mapToTestWiveCall = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000, 60 * 60 * 1000);//todo corect
        // On ajoute les valeurs
        mapToTestWiveCall.put("key1", "value1");
        mapToTestWivoutCall.put("key1", "value1");
        // On mok System.currentTimeMillis() pour simuler le temps
        long entryCreationTime = mapToTestWiveCall.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            long lastUseTime = newCurrentTime - 1;
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(lastUseTime);

            assertTrue(mapToTestWiveCall.containsKey("key1"));
            assertTrue(mapToTestWivoutCall.containsKey("key1"));
            assertEquals(entryCreationTime, mapToTestWiveCall.updateTimeSinceLastUse("key1"));//On met a jour la derniere utilisation sur une map

            theMock.when(TimeHelper::currentTimeMillis).thenReturn(lastUseTime + 1);//On simule le temps pour que la valeur soit supprimée
            assertTrue(mapToTestWiveCall.containsKey("key1"));//La valeur n'est pas supprimée car elle a été utilisée récemment
            assertFalse(mapToTestWivoutCall.containsKey("key1"));//La valeur est supprimée car elle n'a pas été utilisée récemment

            //On verifie que la valeur est supprimée apres 1h
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(lastUseTime + 60 * 60 * 1000 - 1);
            assertTrue(mapToTestWiveCall.containsKey("key1"));
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(lastUseTime + 60 * 60 * 1000);
            assertFalse(mapToTestWiveCall.containsKey("key1"));
        }
        mapToTestWivoutCall.close();
        mapToTestWiveCall.close();

    }

    @Test
    void testDeNetoyageDeLaMap() {
        // on utilise une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        // On ajoute une valeur a la map a tester
        map.put("key1", "value1");
        // On verifie que la map contient bien la valeur
        assertTrue(map.containsKey("key1"));
        // On mok System.currentTimeMillis() pour simuler le temps
        long entryCreationTime = map.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime - 1);
            assertEquals(1, map.sizeWithInvalid());
            map.cleanBlocking();
            assertEquals(1, map.sizeWithInvalid());
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertEquals(1, map.sizeWithInvalid());
            map.cleanBlocking();
            assertEquals(0, map.sizeWithInvalid());
        }
        map.close();
    }

    @Test
    void testDeNetoyageAutomatiqueDeLaMap() {
        // on utilise une duree de vie de 0s est une periode de nettoyage de 100ms
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(0, 0, 100);
        // On ajoute une valeur a la map a tester
        map.put("key1", "value1");
        // On attend 200ms pour que la valeur soit supprimée
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // On verifie que la valeur est supprimée
        assertEquals(0, map.sizeWithInvalid());
        map.close();
    }

    @Test
    void testsetCleanPeriodDesactivateClening() {//todo corect
        // on utilise une duree de vie de 0s est une periode de nettoyage de 100ms
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(0, 0, 100);
        // On change la periode de nettoyage
        map.setCleanPeriod(0);
        // On ajoute une valeur a la map a tester
        map.put("key1", "value1");
        // On attend 200ms pour verifier que la valeur n'est pas supprimée (si la periode de nettoyage est encore a 100ms devrait etre supprimée)
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // On verifie que la valeur n'est pas supprimée
        assertEquals(1, map.sizeWithInvalid());
        map.close();
    }

    @Test
    void testsetCleanPeriodChange() {//todo corect
        // on utilise une duree de vie de 0s est une periode de nettoyage de 100s
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(0, 0, 100_000);
        // On change la periode de nettoyage
        map.setCleanPeriod(100);
        // On ajoute une valeur a la map a tester
        map.put("key1", "value1");
        // On attend 200ms pour que la valeur soit supprimée
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // On verifie que la valeur est supprimée
        assertEquals(0, map.sizeWithInvalid());
        map.close();
    }

    @Test
    void testThreadCleen() {
        String uuid = UUID.randomUUID().toString();
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(0, 0, 100, uuid);
        // asset q'un Thread du nom uuid est lancé
        // print out les threads
        assertTrue(Thread.getAllStackTraces().keySet().stream().anyMatch(thread -> thread.getName().equals(uuid)));
        map.close();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(Thread.getAllStackTraces().keySet().stream().anyMatch(thread -> thread.getName().equals(uuid)));
    }

    @Test
    void testContainsValue() {
        // on utilise une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        HashMap<String, String> hashMap = new HashMap<>();
        map.put("key1", "value1");
        hashMap.put("key1", "value1");
        assertTrue(map.containsValue("value1"));
        assertTrue(hashMap.containsValue("value1"));
        assertFalse(map.containsValue("value2"));
        assertFalse(hashMap.containsValue("value2"));
    }

    @Test
    void testValueWithTime(){
        ValueWithTime<String> valueWithTime = new ValueWithTime<>("value1");
        ValueWithTime<String> valueWithTime2 = new ValueWithTime<>("value1");

        assertTrue(valueWithTime.equals(valueWithTime2));
        assertEquals("value1", valueWithTime.getValue());
        assertEquals("value1".hashCode(), valueWithTime.hashCode());


    }
    @Test
    void testremoveKeyValue() {
        // on utilise une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        HashMap<String, String> hashMap = new HashMap<>();
        map.put("key1", "value1");
        hashMap.put("key1", "value1");
        assertTrue(hashMap.remove("key1", "value1"));
        assertTrue(map.remove("key1", "value1"));
        assertFalse(hashMap.remove("key1", "value1"));
        assertFalse(map.remove("key1", "value1"));
    }

    @Test
    void testRremoveKayValueWhiveExpiredEntry() {
        // on utilise une duree de vie de 0s
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(0);
        map.put("key1", "value1");
        assertFalse(map.remove("key1", "value1"));
    }



    @Test
    void testEntrySetSet() {
        // on utilise une duree de vie de 24h
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        HashMap<String, String> hashMap = new HashMap<>();
        map.put("key1", "value1");
        hashMap.put("key1", "value1");
        map.entrySet().forEach(entry -> {
            entry.setValue("value2");
        });
        hashMap.entrySet().forEach(entry -> {
            entry.setValue("value2");
        });
        assertEquals("value2", map.get("key1"));
        assertEquals("value2", hashMap.get("key1"));

    }
    @Test
    void testIsEmptyWithInvalid() {
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        HashMap<String, String> hashMap = new HashMap<>();
        assertTrue(map.isEmptyWithInvalid());
        assertTrue(hashMap.isEmpty());
        map.put("key1", "value1");
        hashMap.put("key1", "value1");
        assertFalse(map.isEmptyWithInvalid());
        assertFalse(hashMap.isEmpty());
        long entryCreationTime = map.getCreationTimeMillis("key1");
        long newCurrentTime = entryCreationTime + 24 * 60 * 60 * 1000;
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            assertFalse(map.isEmptyWithInvalid());
            assertTrue(map.isEmpty());
        }

    }
    @Test
    void testGetSinceLastUseMillis() {
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(24 * 60 * 60 * 1000);
        map.put("key1", "value1");
        long newCurrentTime = System.currentTimeMillis();
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime +100);
            map.updateTimeSinceLastUse("key1");
            assertEquals(newCurrentTime +100, map.getSinceLastUseMillis("key1"));
        }
    }
    @Test
    void testForEach() {
        ConcurrentHashMapAutoCleaning<String, String> map = new ConcurrentHashMapAutoCleaning<>(Long.MAX_VALUE);
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put("key" + i, "value" + i);
            hashMap.put("key" + i, "value" + i);
        }
        map.forEach((key, value) -> {
            map.put(key, "value2");
        });
        hashMap.forEach((key, value) -> {
            hashMap.put(key, "value2");
        });
        map.close();

        long newCurrentTime = System.currentTimeMillis();
        // On simule le temps
        try (MockedStatic<TimeHelper> theMock = Mockito.mockStatic(TimeHelper.class)) {
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            ConcurrentHashMapAutoCleaning<String, String> map2 = new ConcurrentHashMapAutoCleaning<>(24*60*60*1000);
            HashMap<String, String> hashMap2 = new HashMap<>();
            for (int i = 0; i < 100; i++) {
                map2.put("key" + i, "value" + i);
                hashMap2.put("key" + i, "value" + i);
            }
            assertEquals(map2, hashMap2);

            newCurrentTime =newCurrentTime+ 24 * 60 * 60 * 1000 + 1;
            theMock.when(TimeHelper::currentTimeMillis).thenReturn(newCurrentTime);
            AtomicInteger i = new AtomicInteger();
            map2.forEach((key, value) -> {
                i.getAndIncrement();
            });
            AtomicInteger j = new AtomicInteger();
            hashMap2.forEach((key, value) -> {
                j.getAndIncrement();
            });
            assertEquals(0, i.get());
            assertEquals(100, j.get());
            assertNotEquals(map2, hashMap2);

        }
    }
}
