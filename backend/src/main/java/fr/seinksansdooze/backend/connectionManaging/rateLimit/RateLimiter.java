package fr.seinksansdooze.backend.connectionManaging.rateLimit;

import fr.seinksansdooze.backend.connectionManaging.ConcurrentHashMapAutoCleaning;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.io.Closeable;
import java.time.Duration;
/**
 * Cette classe implémente un rate limiter pour limiter le nombre de requêtes qu'un utilisateur peut effectuer dans un intervalle de temps donné.
 * Elle utilise la bibliothèque Bucket4J pour créer et gérer des seaux individuels pour chaque utilisateur.
 */
public class RateLimiter implements Closeable {
    /**
     * Nombre maximal de requêtes autorisées dans une période de temps donnée.
     */
    private final int maxRequests;
    /**
     * Durée de la période de temps pour limiter le nombre de requêtes.
     */
    private final int timeWindow;
    /**
     * ConcurrentHashMapAutoCleaning utilisé pour stocker des seaux (buckets) individuels pour chaque utilisateur.
     * Les éléments sont nettoyés automatiquement après la durée spécifiée.
     */
    private final ConcurrentHashMapAutoCleaning<String, Bucket> rateLimiters;

    /**
     * Initialise un nouvel objet RateLimiter avec les paramètres spécifiés.
     *
     * @param maxRequests Le nombre maximal de requêtes autorisées dans une période de temps donnée.
     * @param timeWindow  La durée de la période de temps pour limiter le nombre de requêtes.
     */
    public RateLimiter(int maxRequests, int timeWindow) {
        this.maxRequests = maxRequests;
        this.timeWindow = timeWindow;
        this.rateLimiters = new ConcurrentHashMapAutoCleaning<>(timeWindow*1000L,timeWindow*1000L,timeWindow*1000L);
    }

    /**
     * Méthode privée qui crée un seau (bucket) avec un débit (bandwidth) et une capacité (limit) spécifiés.
     *
     * @return Le seau (bucket) créé.
     */
    private Bucket bucketCreator() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(maxRequests, Refill.intervally(maxRequests, Duration.ofSeconds(timeWindow))))
                .build();
    }
    /**
     * Tente de consommer un jeton dans le seau correspondant à la clé spécifiée.
     * Si le seau existe, cette méthode tente de consommer un jeton. Si le seau n'existe pas,
     * un nouveau seau est créé pour la clé et un jeton est consommé.
     * Si le seau a suffisamment de jetons pour consommer un jeton supplémentaire,
     * la méthode renvoie true. Sinon, elle renvoie false.
     *
     * @param key la clé qui correspond au seau à vérifier.
     * @return true si un jeton a été consommé avec succès et si le seau a suffisamment de jetons pour consommer un autre jeton, sinon false.
     */
    public boolean tryConsume(String key) {
        if (rateLimiters.containsKey(key)) {
            return rateLimiters.get(key).tryConsume(1);
        } else {
            Bucket bucket = bucketCreator();
            rateLimiters.put(key, bucket);
            return bucket.tryConsume(1);
        }
    }
    /**
     * cette fonction permet de désactiver le nettoyage
     * il est important de l'appeler une fois que l'on a plus besoin de l'objet pour fermer le tread
     */
    @Override
    public void close() {
        rateLimiters.close();
    }
}