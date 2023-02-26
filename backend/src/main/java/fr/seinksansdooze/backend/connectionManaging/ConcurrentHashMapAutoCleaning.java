package fr.seinksansdooze.backend.connectionManaging;


import java.io.Closeable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
    private final ConcurrentMap<K, ValueWithTime<V>> map = new ConcurrentHashMap<>();
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
     * @param lifeTimeMillis             le temps de vie des entrées en secondes
     * @param lifeTimeSinceLastUseMillis le temps de vie depuis la dernière utilisation en secondes
     * @param cleanPeriodMillis          le temps entre chaque nettoyage en millisecondes
     */
    public ConcurrentHashMapAutoCleaning(long lifeTimeMillis, long lifeTimeSinceLastUseMillis, long cleanPeriodMillis, String threadName) {
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
     */
    public long getCreationTimeMillis(Object key) {
        ValueWithTime<V> valueWithTime = map.get(key);
        if (valueWithTime == null) {
            return -1;
        }
        return valueWithTime.getCreationTimeMillis();
    }

    /**
     * Update la date de dernière utilisation de l'entrée en millisecondes
     *
     * @param key la clef de l'entrée
     * @return l'ancienne date de dernière utilisation de l'entrée en millisecondes ou -1 si l'entrée n'existe pas
     */
    public long updateTimeSinceLastUse(Object key) {//todo test
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
        for (Entry<K, ValueWithTime<V>> entry : map.entrySet()) {
            if (!entry.getValue().isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                map.remove(entry.getKey());
            }
        }
    }

    private Timer createNamedTimer() {
        Timer timer = new Timer(threadName,true);
        timer.schedule(new TimerTask() {
            public void run() {
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            }
        }, 0);
        return timer;
    }

    /**
     * cette fonction permet de changer le temps entre chaque nettoyage de la map ou de désactiver le nettoyage en mettant la valeur 0
     * @param cleanPeriodMillis le temps entre chaque nettoyage en millisecondes
     */
    public void setCleanPeriod(long cleanPeriodMillis) {
        if (this.cleanPeriodMillis == cleanPeriodMillis) {
            return;
        }
        this.cleanPeriodMillis = cleanPeriodMillis;
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (cleanPeriodMillis > 0) {
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
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }

        }
    }

    /**
     * cette fonction permet de désactiver le nettoyage
     * il est important de l'appeler une fois que l'on a plus besoin de l'objet pour fermer le tread
     */
    public void close() {
        setCleanPeriod(0);
    }

    /**
     * Sette fonction peut etre longue si la map est grande
     * Retourne la taille de la map
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
     * Sette fonction peut etre longue si la map est grande
     * Retourne true si la map est vide
     * @return true si la map est vide
     */
    @Override
    public boolean isEmpty() {//todo pas bon si on a des entrées invalides faire 2 fonctions
        for (ValueWithTime<V> valueWithTime : map.values()) {
            if (valueWithTime.isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Retourne true si la map est vide avec les entrées invalides
     * @return true si la map est vide avec les entrées invalides
     */
    public boolean isEmptyWithInvalid() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {//todo optimiser
        return map.containsKey(key) && map.get(key).isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis);
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
        ValueWithTime<V> valueWithTime = new ValueWithTime<>(value);
        ValueWithTime<V> oldValue = map.put(key, valueWithTime);
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
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        //création d'un set qui contient les clefs valides
        Set<K> set = new HashSet<>();
        for (Entry<K, ValueWithTime<V>> entry : map.entrySet()) {
            if (entry.getValue().isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                set.add(entry.getKey());
            }
        }
        return set;
    }

    @Override
    public Collection<V> values() {
        //création d'une collection qui contient les valeurs valides
        Collection<V> collection = new ArrayList<>();
        for (Entry<K, ValueWithTime<V>> entry : map.entrySet()) {
            if (entry.getValue().isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                collection.add(entry.getValue().getValue());
            }
        }
        return collection;
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
                        return entry.getValue().setValue(value);
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
                        if (!(obj instanceof Map.Entry))
                            return false;
                        Map.Entry<?, ?> e = (Map.Entry<?, ?>) obj;
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
            return v.getValue();
        }

    }

    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
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

    @Override
    public boolean equals(Object obj) {//todo verif
        if (obj == this)
            return true;

        if (!(obj instanceof Map))
            return false;
        Map<?, ?> m = (Map<?, ?>) obj;
        for (Entry<K, ValueWithTime<V>> entry : map.entrySet()) {
            if (entry.getValue().isValid(lifeTimeMillis, lifeTimeSinceLastUseMillis)) {
                K key = entry.getKey();
                V value = entry.getValue().getValue();
                if (value == null) {
                    if (!(m.containsKey(key) && m.get(key) == null))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        }
        return true;
    }
}


/**
 * Cette classe permet de stocker une valeur avec un temps de création et un temps de dernière utilisation
 * Elle est utilisée par la classe ConcurrentHashMapAutoCleaning
 *
 * @param <v>
 */
class ValueWithTime<v> {
    private v value;
    private long creationTimeMillis;
    private long timeSinceLastUseMillis;

    /**
     * Constructeur
     *
     * @param value la valeur à stocker
     */
    public ValueWithTime(v value) {
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
     * Cette méthode permet de modifier la valeur
     *
     * @param value la nouvelle valeur
     */
    public v setValue(v value) {
        creationTimeMillis = TimeHelper.currentTimeMillis();
        timeSinceLastUseMillis = creationTimeMillis;
        v oldValue = this.value;
        this.value = value;
        return oldValue;
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
        return Objects.equals(value, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}