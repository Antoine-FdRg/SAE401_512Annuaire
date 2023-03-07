package fr.seinksansdooze.backend.connectionManaging;


import java.io.Closeable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
//todo add doc

/**
 * <strong>/!\ Il est important de close cette classe pour arrêter le nettoyage automatique/!\</strong>
 * Cette classe étend la classe ConcurrentHashMap et permet de créer une map qui nettoie automatiquement les entrées
 * qui ont dépassé un certain temps de vie.
 * Le temps de vie des entrées doit être défini dans le constructeur.
 * Il est possible de définir le temps de vie depuis la dernière utilisation.
 */
public class ConcurrentHashMapAutoCleaning<K, V> implements ConcurrentMap<K, V>, Closeable {

    /**
     * Une Map qui contient les clefs et un ValueWithTime de la valeur.
     * on utilise ValueWithTime pour pouvoir stocker la date de création et la date de dernière utilisation
     */
    private final ConcurrentMap<K, ValueWithTime<V>> map ;
    /**
     * Temps de vie des entrées en secondes
     */
    private final long lifeTimeMillis;
    /**
     * Temps de vie depuis la dernière utilisation en secondes
     */
    private final long lifeTimeSinceLastUseMillis;
    /**
     * Nom du thread
     */
    private final String threadName;
    /**
     * Temps entre chaque nettoyage en millisecondes
     */
    private long cleanPeriodMillis = -1;
    /**
     * TimerTask pour nettoyer la map
     */
    private TimerTask timerTask;
    /**
     * Timer pour nettoyer la map
     */
    private Timer timer;
//todo add param to constructor for HashMap params

    /**
     * Constructeur le nettoyage sera lancé a la création de l'objet si le temps entre chaque nettoyage est supérieur à 0
     *
     * @param lifeTimeMillis             le temps de vie des entrées en millisecondes
     * @param lifeTimeSinceLastUseMillis le temps de vie depuis la dernière utilisation en millisecondes
     * @param cleanPeriodMillis          le temps entre chaque nettoyage en millisecondes
     * @param threadName                 le nom du thread
     * @param initialCapacity the initial capacity. The implementation
     * performs internal sizing to accommodate this many elements,
     * given the specified load factor.
     * @param loadFactor the load factor (table density) for
     * establishing the initial table size
     * @param concurrencyLevel the estimated number of concurrently
     * updating threads. The implementation may use this value as
     * a sizing hint.
     * @throws IllegalArgumentException if the initial capacity is
     * negative or the load factor or concurrencyLevel are
     * nonpositive
     */
    public ConcurrentHashMapAutoCleaning(long lifeTimeMillis, long lifeTimeSinceLastUseMillis, long cleanPeriodMillis,int initialCapacity,
                                         float loadFactor, int concurrencyLevel, String threadName) {
        this.map = new ConcurrentHashMap<>(initialCapacity,loadFactor,concurrencyLevel);
        this.lifeTimeMillis = lifeTimeMillis;
        this.lifeTimeSinceLastUseMillis = lifeTimeSinceLastUseMillis;
        this.threadName = threadName;
        setCleanPeriod(cleanPeriodMillis);

    }

    /**
     * Constructeur le nettoyage sera lancé a la création de l'objet si le temps entre chaque nettoyage est supérieur à 0
     *
     * @param lifeTimeMillis             le temps de vie des entrées en secondes
     * @param lifeTimeSinceLastUseMillis le temps de vie depuis la dernière utilisation en secondes
     * @param cleanPeriodMillis          le temps entre chaque nettoyage en millisecondes
     * @param threadName                 le nom du thread
     * @param initialCapacity the initial capacity. The implementation
     * performs internal sizing to accommodate this many elements,
     * given the specified load factor.
     * @param loadFactor the load factor (table density) for
     * establishing the initial table size
     * @throws IllegalArgumentException if the initial capacity is
     * negative or the load factor or concurrencyLevel are
     * nonpositive
     */
    public ConcurrentHashMapAutoCleaning(long lifeTimeMillis, long lifeTimeSinceLastUseMillis, long cleanPeriodMillis,int initialCapacity,
                                         float loadFactor, String threadName) {
        // Default value for concurrencyLevel is 1
        this(lifeTimeMillis, lifeTimeSinceLastUseMillis, cleanPeriodMillis, initialCapacity,loadFactor,1,threadName);

    }

    /**
     * Constructeur le nettoyage sera lancé a la création de l'objet si le temps entre chaque nettoyage est supérieur à 0
     *
     * @param lifeTimeMillis             le temps de vie des entrées en secondes
     * @param lifeTimeSinceLastUseMillis le temps de vie depuis la dernière utilisation en secondes
     * @param cleanPeriodMillis          le temps entre chaque nettoyage en millisecondes
     * @param threadName                 le nom du thread
     * @param initialCapacity the initial capacity. The implementation
     * performs internal sizing to accommodate this many elements,
     * given the specified load factor.
     * @throws IllegalArgumentException if the initial capacity of elements is negative
     */
    public ConcurrentHashMapAutoCleaning(long lifeTimeMillis, long lifeTimeSinceLastUseMillis, long cleanPeriodMillis,int initialCapacity, String threadName) {
        // Default value for loadFactor is 0.75f and concurrencyLevel is 1
        this(lifeTimeMillis, lifeTimeSinceLastUseMillis, cleanPeriodMillis,initialCapacity,0.75f,1, threadName);

    }

    /**
     * Constructeur le nettoyage sera lancé a la création de l'objet si le temps entre chaque nettoyage est supérieur à 0
     *
     * @param lifeTimeMillis             le temps de vie des entrées en secondes
     * @param lifeTimeSinceLastUseMillis le temps de vie depuis la dernière utilisation en secondes
     * @param cleanPeriodMillis          le temps entre chaque nettoyage en millisecondes
     */
    public ConcurrentHashMapAutoCleaning(long lifeTimeMillis, long lifeTimeSinceLastUseMillis, long cleanPeriodMillis, String threadName) {
        // Default values for initialCapacity, loadFactor and concurrencyLevel are 16, 0.75f and 1 respectively
        this(lifeTimeMillis, lifeTimeSinceLastUseMillis, cleanPeriodMillis,16,0.75f,1, threadName);


    }

    /**
     * Constructeur le nettoyage sera lancé a la création de l'objet si le temps entre chaque nettoyage est supérieur à 0
     *
     * @param lifeTimeMillis             le temps de vie des entrées en secondes
     * @param lifeTimeSinceLastUseMillis le temps de vie depuis la dernière utilisation en secondes
     * @param cleanPeriodMillis          le temps entre chaque nettoyage en millisecondes
     */
    public ConcurrentHashMapAutoCleaning(long lifeTimeMillis, long lifeTimeSinceLastUseMillis, long cleanPeriodMillis) {
        this(lifeTimeMillis, lifeTimeSinceLastUseMillis, cleanPeriodMillis, "ConcurrentHashMapAutoCleaning");

    }

    /**
     * Constructeur
     *
     * @param lifeTimeMillis             le temps de vie des entrées en secondes
     * @param lifeTimeSinceLastUseMillis le temps de vie depuis la dernière utilisation en secondes
     */
    public ConcurrentHashMapAutoCleaning(long lifeTimeMillis, long lifeTimeSinceLastUseMillis) {
        this(lifeTimeMillis, lifeTimeSinceLastUseMillis, -1);
    }

    /**
     * Constructeur
     *
     * @param lifeTimeMillis le temps de vie des entrées en secondes
     */
    public ConcurrentHashMapAutoCleaning(long lifeTimeMillis) {
        this(lifeTimeMillis, 0, -1);
    }


    /**
     * Retourne la date de création de l'entrée en millisecondes
     *
     * @param key la clef de l'entrée
     * @return la date de création de l'entrée en millisecondes ou -1 si l'entrée n'existe pas
     * @throws ClassCastException if the key is of an inappropriate type for this map
     */
    public long getCreationTimeMillis(Object key) {
        ValueWithTime<V> valueWithTime = map.get(key);
        if (valueWithTime == null) {
            return -1;
        }
        return valueWithTime.getCreationTimeMillis();
    }

    /**
     * Retourne le temps de la dernière utilisation de l'entrée en millisecondes
     * @param key la clef de l'entrée
     * @return le temps de la dernière utilisation de l'entrée en millisecondes ou -1 si l'entrée n'existe pas
     */

    public long getSinceLastUseMillis(Object key) {
        ValueWithTime<V> valueWithTime = map.get(key);
        if (valueWithTime == null) {
            return -1;
        }
        return valueWithTime.getTimeSinceLastUseMillis();
    }

    /**
     * Update la date de dernière utilisation de l'entrée en millisecondes
     *
     * @param key la clef de l'entrée
     * @return l'ancienne date de dernière utilisation de l'entrée en millisecondes ou -1 si l'entrée n'existe pas
     * @throws ClassCastException if the key is of an inappropriate type for this map
     */
    public long updateTimeSinceLastUse(Object key) {
        ValueWithTime<V> valueWithTime = map.get(key);
        if (valueWithTime != null) {
            return valueWithTime.updateTimeSinceLastUse();
        }
        return -1;
    }

    /**
     * cette méthode permet de nettoyer la map des entrées qui ont dépassé le temps de vie
     * sette méthode est <strong>bloquante</strong> preferer la méthode setCleanPeriod(long cleanPeriodMillis) pour
     * nettoyer la map automatiquement si un cleanPeriodMillis n'as pas été défini dans le constructeur
     */
    public void cleanBlocking() {
        Iterator<K> iterator = map.keySet().iterator();
        // Parcourir toutes les clés de la vue et supprimer les entrées invalides.
        while (iterator.hasNext()) {
            K key = iterator.next();
            ValueWithTime<V> valueWithTime = map.get(key);
            if (!valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                iterator.remove(); // Supprimer l'entrée avec l'itérateur.
            }
        }
    }


    /**
     Creates a named Timer with a specified thread name and sets the thread's priority to minimum.
     @return a Timer object
     */
    private Timer createNamedTimer() {
        Timer timer = new Timer(threadName, true);
        timer.schedule(new TimerTask() {
            public void run() {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            }
        }, 0);
        return timer;
    }



    /**
     * cette fonction permet de changer le temps entre chaque nettoyage de la map ou de désactiver le nettoyage en mettant la valeur 0
     *
     * @param cleanPeriodMillis le temps entre chaque nettoyage en millisecondes
     */
    public void setCleanPeriod(long cleanPeriodMillis) {
        if (cleanPeriodMillis <= 0) {
            desactiverNettoyage();
            return;
        }
        if (cleanPeriodMillis == this.cleanPeriodMillis) {
            return;
        }
        this.cleanPeriodMillis = cleanPeriodMillis;
        reprogrammerNettoyage();
    }

    private void desactiverNettoyage() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private void reprogrammerNettoyage() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (timer == null) {
            timer = createNamedTimer();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                cleanBlocking();
            }
        };
        timer.schedule(timerTask, 0, cleanPeriodMillis);
    }



    /**
     * cette fonction permet de désactiver le nettoyage
     * il est important de l'appeler une fois que l'on a plus besoin de l'objet pour fermer le tread
     */
    @Override
    public void close() {
        desactiverNettoyage();
    }

    /**
     * Sette fonction peut etre longue si la map est grande
     * Retourne la taille de la map
     *
     * @return la taille de la map
     */
    @Override
    public int size() {
        int size = 0;
        for (ValueWithTime<V> valueWithTime : map.values()) {
            if (valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                size++;
            }
        }
        return size;
    }

    /**
     * Retourne la taille de la map avec les entrées invalides
     *
     * @return la taille de la map avec les entrées invalides
     */
    public int sizeWithInvalid() {
        return map.size();
    }

    /**
     *
     * Sette fonction peut etre longue si la map est grande
     * Retourne true si la map est vide
     * Cette méthode parcourt manuellement les valeurs de la map plutôt que d'utiliser la méthode size() de la classe ConcurrentHashMap,
     * car il est possible que certaines entrées invalides n'aient pas encore été retirées de la map.
     * il existe la foncion isEmptyWithInvalid() pour savoir si la map est vide avec les entrées invalides
     * @return true si la map est vide
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        for (ValueWithTime<V> valueWithTime : map.values()) {
            if (valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Retourne true si la map est vide avec les entrées invalides
     *
     * @return true si la map est vide avec les entrées invalides
     */
    public boolean isEmptyWithInvalid() {//todo test
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        ValueWithTime<V> valueWithTime = map.get(key);
        return valueWithTime != null && valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis);
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null)
            throw new NullPointerException();
        for (ValueWithTime<V> valueWithTime : map.values()) {
            if (valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis) && valueWithTime.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        if (key == null)
            throw new NullPointerException();
        ValueWithTime<V> valueWithTime = map.get(key);
        if (valueWithTime == null) {
            return null;
        }
        if (valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
            return valueWithTime.getValue();
        } else {
            map.remove(key);
            return null;
        }
    }


    @Override
    public V put(K key, V value) {
        if (key == null || value == null)
            throw new NullPointerException();
        ValueWithTime<V> oldValue = map.put(key, new ValueWithTime<>(value));
        if (oldValue == null) {
            return null;
        } else if (oldValue.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
            return oldValue.getValue();
        } else {
            return null;
        }
    }

    @Override
    public V remove(Object key) {
        if (key == null)
            throw new NullPointerException();
        ValueWithTime<V> valueWithTime = map.remove(key);
        if (valueWithTime == null) {
            return null;
        }
        if (valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
            return valueWithTime.getValue();
        } else {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == null)
            throw new NullPointerException();
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    /**
     * Retourne un ensemble contenant les clés des entrées valides de la map.
     * Cette méthode parcourt manuellement les entrées de la map pour s'assurer que seules les clés valides sont retournées.
     * Cette fonction parcourt chaque entrée de la map et ajoute la clé de l'entrée à un nouvel ensemble si l'entrée est
     * valide. Un nouvel HashSet est créé pour stocker ces clés valides,<strong> ce qui signifie que les modifications apportées
     * à l'ensemble retourné n'affecteront pas la map elle-même.</strong>
     * @return un ensemble contenant les clés des entrées valides de la map
     */
    @Override
    public Set<K> keySet() {
        //création d'un set qui contient les clefs valides
        return map.keySet().stream()
                .filter(key -> map.get(key).isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis))
                .collect(Collectors.toSet());
    }

    /**
     * Retourne une collection contenant toutes les valeurs valides de la map.
     * Cette méthode parcourt manuellement les entrées de la map pour s'assurer que seules les clés valides sont retournées.
     * Cette fonction parcourt chaque entrée de la map et ajoute la clé de l'entrée à un nouvel ensemble si l'entrée est
     * valide. Un nouvel HashSet est créé pour stocker ces clés valides, <strong> ce qui signifie que les modifications apportées
     * à l'ensemble retourné n'affecteront pas la map elle-même.</strong>
     * @return une collection contenant toutes les valeurs valides de la map
     */
    @Override
    public Collection<V> values() {
        //création d'une collection qui contient les valeurs valides
        return map.values().stream()
                .filter(valueWithTime -> valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis))
                .map(ValueWithTime::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        //création d'un set qui contient les entrées valides
        Set<Entry<K, V>> set = new HashSet<>();
        for (Entry<K, ValueWithTime<V>> entry : map.entrySet()) {
            if (entry.getValue().isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                set.add(new Entry<K, V>() {
                    @Override
                    public K getKey() {
                        return entry.getKey();
                    }

                    @Override
                    public V getValue() {
                        return entry.getValue().getValue();
                    }

                    @Override
                    public V setValue(V value) {
                        return map.replace(getKey(), new ValueWithTime<>(value)).getValue();
                    }

                    @Override
                    public String toString() {
                        return getKey() + "=" + getValue();
                    }

                    @Override
                    public int hashCode() {
                        return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
                    }

                    @Override
                    public boolean equals(Object obj) {
                        if (!(obj instanceof Map.Entry<?, ?> e))
                            return false;
                        return Objects.equals(getKey(), e.getKey()) &&
                                Objects.equals(getValue(), e.getValue());
                    }
                });
            }
        }
        return set;
    }


    @Override
    public V putIfAbsent(K key, V value) {
        ValueWithTime<V> v = map.putIfAbsent(key, new ValueWithTime<>(value));
        if (v == null) {
            return null;
        } else {
            if (v.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                return v.getValue();
            } else {
                map.remove(key);
                return null;
            }
        }

    }

    @Override
    public boolean remove(Object key, Object value) {
        if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
            map.remove(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, oldValue) ||
                (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
        return true;
    }

    @Override
    public V replace(K key, V value) {
        V curValue;
        if (((curValue = get(key)) != null) || containsKey(key)) {
            curValue = put(key, value);
        }
        return curValue;
    }

//    @Override
//    public boolean equals(Object obj) {//todo verif todo test
//        if (obj == this)
//            return true;
//        if (!(obj instanceof Map<?, ?> m))
//            return false;
//        for (Entry<K, ValueWithTime<V>> entry : map.entrySet()) {
//            if (entry.getValue().isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
//                K key = entry.getKey();
//                V value = entry.getValue().getValue();
//                if (value == null) {
//                    if (!(m.containsKey(key) && m.get(key) == null))
//                        return false;
//                } else {
//                    if (!value.equals(m.get(key)))
//                        return false;
//                }
//            }
//        }
//        return true;
//    }
    @Override
public boolean equals(Object o) {
    if (o == this)
        return true;

    if (!(o instanceof Map<?, ?> m))
        return false;
    if (m.size() != size())
        return false;

    try {
        for (Entry<K, V> e : entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            if (value == null) {
                if (!(m.get(key) == null && m.containsKey(key)))
                    return false;
            } else {
                if (!value.equals(m.get(key)))
                    return false;
            }
        }
    } catch (ClassCastException unused) {
        return false;
    } catch (NullPointerException unused) {
        return false;
    }

    return true;
}
}



/**
 * Cette classe permet de stocker une valeur avec un temps de création et un temps de dernière utilisation
 * Elle est utilisée par la classe ConcurrentHashMapAutoCleaning
 * @param <v>
 */
class ValueWithTime<v>{
    private v value;
    private long creationTimeMillis;
    private long timeSinceLastUseMillis;

    /**
     * Constructeur
     *
     * @param value la valeur à stocker
     */
    public ValueWithTime(v value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot be null");
        }
        this.value = value;
        this.creationTimeMillis = TimeHelper.currentTimeMillis();
        this.timeSinceLastUseMillis = creationTimeMillis;
    }

    /**
     * Cette méthode permet de récupérer la valeur
     *
     * @return la valeur
     */
    public v getValue() {
        return value;
    }

    /**
     * Cette méthode permet de mettre à jour le temps de dernière utilisation
     *
     * @return l'ancien temps de dernière utilisation
     */
    public long updateTimeSinceLastUse() {
        long oldTimeSinceLastUseMillis = timeSinceLastUseMillis;
        timeSinceLastUseMillis = TimeHelper.currentTimeMillis();
        return oldTimeSinceLastUseMillis;
    }

    /**
     * Cette méthode permet de récupérer le temps de création
     *
     * @return le temps de création
     */
    public long getCreationTimeMillis() {
        return creationTimeMillis;
    }

    /**
     * Cette méthode permet de récupérer le temps de dernière utilisation
     *
     * @return le temps de dernière utilisation
     */
    public long getTimeSinceLastUseMillis() {//todo
        return timeSinceLastUseMillis;
    }

    /**
     * Cette méthode permet de savoir si l'entrée est encore valide
     */
    public boolean isValid(long lifeTimeMillis, long lifeTimeSinceLastUseMillis) {
        long currentTimeMillis = TimeHelper.currentTimeMillis();
        return currentTimeMillis - creationTimeMillis < lifeTimeMillis || currentTimeMillis - timeSinceLastUseMillis < lifeTimeSinceLastUseMillis;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ValueWithTime<?>){
            return value.equals(((ValueWithTime<?>) o).getValue());
        }
        return value.equals(o);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}