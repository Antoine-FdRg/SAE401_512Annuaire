package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
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
