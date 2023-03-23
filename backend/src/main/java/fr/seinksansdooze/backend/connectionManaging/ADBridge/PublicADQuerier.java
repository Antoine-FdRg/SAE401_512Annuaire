package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IPublicADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.model.ObjectType;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;

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
     * @param searchedName le nom de la personne recherchée
     * @return une liste de personnes correspondant à la recherche
     */
    public ArrayList<PartialPerson> searchPerson(String searchedName) {
        NamingEnumeration<SearchResult> res = this.search(ObjectType.PERSON, searchedName);
        ArrayList<PartialPerson> persons = new ArrayList<>();
        try {
            while (res.hasMore()) {
                SearchResult currentPerson = res.next();
                PartialPerson person = new PartialPerson(currentPerson);
                persons.add(person);
            }
            return persons;
        } catch (NamingException e) {
            return persons;
        }
    }

    /**
     * Méthode répondant à la route GET /api/public/search/structure
     *
     * @param searchedName le nom de la structure recherchée
     * @return une liste de structures correspondant à la recherche
     */
    public ArrayList<PartialStructure> searchStructure(String searchedName) {
        NamingEnumeration<SearchResult> res = this.search(ObjectType.STRUCTURE, searchedName);
        ArrayList<PartialStructure> structures = new ArrayList<>();
        try {
            while (res.hasMore()) {
                SearchResult currentStructure = res.next();
                PartialStructure structure = new PartialStructure(currentStructure);
                structures.add(structure);
            }
            return structures;
        } catch (NamingException e) {
            return structures;
        }
    }

    /**
     * Méthode répondant à la route GET /api/public/info/person/{cn}
     *
     * @param cn le cn de la personne recherchée
     * @return une personne correspondant à la recherche
     */
    //TODO change cn by uuid
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
