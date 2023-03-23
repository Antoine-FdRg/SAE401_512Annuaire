package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PublicADQuerier extends ADQuerier implements IPublicADQuerier {

    @Autowired
    public PublicADQuerier(
            @Value("${public.username}") String username,
            @Value("${public.password}") String password
    ) {
        super(username, password);
    }

    /**
     * Déroule les résultats d'une recherche LDAP, pagine-les et les place dans une liste.
     *
     * @param searchResults    Une énumération de résultats de recherche LDAP à dérouler. Si elle est nulle, une liste
     *                         vide est automatiquement retournée.
     * @param page             Le numéro de la page de résultats à retourner (commençant à zéro).
     * @param perPage          Le nombre d'éléments par page à retourner.
     * @param listContentClass La classe d'objet à instancier pour chaque résultat de recherche. Cette classe doit avoir
     *                         un constructeur prenant un objet SearchResult en paramètre.
     * @return Une liste d'objets correspondants au contenu de la liste spécifiée, paginée selon les paramètres
     * spécifiés. Si aucun résultat n'est trouvé, une liste vide est renvoyée.
     */
    @SneakyThrows
    private static List<?> unroll(NamingEnumeration<SearchResult> searchResults, int page, int perPage, Class<?> listContentClass) {
        if (searchResults == null) return List.of();

        List<Object> items = new ArrayList<>();

        boolean hasMore;
        while (true) {
            try {
                hasMore = searchResults.hasMore();
            } catch (NamingException e) {
                hasMore = false;
            }
            if (!hasMore) break;

            SearchResult currentItem;
            try {
                currentItem = searchResults.next();
            } catch (NamingException e) {
                break;
            }

            Object content = listContentClass.getDeclaredConstructor(SearchResult.class).newInstance(currentItem);
            items.add(content);
        }

        // On fait la pagination
        int startIndex = page * perPage;
        if (startIndex > items.size()) return List.of();

        int endIndex = Math.min(startIndex + perPage, items.size());

        return items.subList(startIndex, endIndex);
    }

    /**
     * Méthode répondant à la route GET /api/public/search/person
     *
     * @param searchedName Le nom de la personne recherchée
     * @param page         Le numéro de page à récuperer.
     * @param perPage      Le nombre d'objets à récupérer par page.
     * @return Une liste de personnes correspondant à la recherche
     */
    @SuppressWarnings("unchecked")
    public List<PartialPerson> searchPerson(String searchedName, int page, int perPage) {
        NamingEnumeration<SearchResult> res = this.search(ObjectType.PERSON, searchedName);

        return (List<PartialPerson>) unroll(res, page, perPage, PartialPerson.class);
    }

    /**
     * Méthode répondant à la route GET /api/public/search/structure
     *
     * @param searchedName le nom de la structure recherchée.
     * @param page         Le numéro de page à récuperer.
     * @param perPage      Le nombre d'objets à récupérer par page.
     * @return une liste de structures correspondant à la recherche
     */
    @SuppressWarnings("unchecked")
    public List<PartialStructure> searchStructure(String searchedName, int page, int perPage) {
        NamingEnumeration<SearchResult> res = this.search(ObjectType.STRUCTURE, searchedName);

        return (List<PartialStructure>) unroll(res, page, perPage, PartialStructure.class);
    }

    /**
     * Méthode répondant à la route GET /api/public/info/person/{cn}
     *
     * @param cn le cn de la personne recherchée
     * @return une personne correspondant à la recherche
     */
    public PartialPerson getPartialPersonInfo(String cn) {
        String filter = "(&(objectClass=user)(CN=" + cn + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                SearchResult currentPerson = res.next();
                return new PartialPerson(currentPerson);
            }
            return null;
        } catch (NamingException e) {
            return null;
        }
    }

    /**
     * Méthode répondant à la route GET /api/public/info/structure/{ou}
     *
     * @param ou le ou de la structure recherchée
     * @return une structure correspondant à la recherche
     */
    @Override
    public PartialStructure getPartialStructureInfo(String ou) {
        String filter = "(&(objectClass=organizationalUnit)(distinguishedName=" + ou + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                SearchResult currentStructure = res.next();
                return new PartialStructure(currentStructure);
            }
            return null;
        } catch (NamingException e) {
            return null;
        }
    }
}
