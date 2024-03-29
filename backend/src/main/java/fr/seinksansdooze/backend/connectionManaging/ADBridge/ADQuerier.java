package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.model.ObjectType;
import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBadRequest;
import fr.seinksansdooze.backend.model.exception.group.SeinkSansDoozeGroupNotFound;
import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserNotFound;
import fr.seinksansdooze.backend.model.response.LoggedInUser;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
public abstract class ADQuerier {
    private static final String AD_URL = "ldap://10.22.32.2:389/";  //389 (search et createSubcontext) ou 3268 (search only) ou 636 (SSL)
    protected static final String AD_BASE = "OU=512BankFR,DC=EQUIPE1B,DC=local";
    protected static final String AD_GROUP_BASE = "CN=Users,DC=EQUIPE1B,DC=local";

    protected DirContext context;

    protected ADQuerier(String username, String pwd) {
        boolean connected = this.login(username, pwd) != null;
        if (!connected) {
            throw new SeinkSansDoozeBackException(HttpStatus.NOT_ACCEPTABLE, "Impossible de se connecter à l'annuaire Active Directory");
        }
    }

    public ADQuerier() {
    }

    /////////////////////////// Méthode de connexion et de déconnexion ///////////////////////////

    /**
     * Méthode répondant à la route /api/auth/login en connectant l'utilisateur à l'annuaire Active Directory
     *
     * @param username le nom d'utilisateur
     * @param pwd      le mot de passe
     * @return Un objet LoggedInUser contenant les informations de l'utilisateur connecté
     */
    public LoggedInUser login(String username, String pwd) {
        Properties env = new Properties();
        username = username + "@EQUIPE1B.local";
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, AD_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, pwd);
        try {
            this.context = new InitialDirContext(env);
            return getLoggedInUser(username);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect", e);
        }
    }

    private LoggedInUser getLoggedInUser(String username) {
        try {
            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(&(objectClass=user)(userPrincipalName=" + username + "))";
            NamingEnumeration<SearchResult> res;
            res = this.context.search(AD_BASE, filter, searchControls);
            return new LoggedInUser(res.next());
        } catch (NamingException e) {
            throw new SeinkSansDoozeUserNotFound();
        }
    }

    /**
     * Méthode répondant à la route /api/auth/logout en déconnectant l'utilisateur de l'annuaire Active Directory
     */
    public void logout() {
        try {
            this.context.close();
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la déconnexion");
        }
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
    protected static List<?> unroll(NamingEnumeration<SearchResult> searchResults, int page, int perPage, Class<?> listContentClass) {
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
     * Effectue des <code>context.search()</code> de façon sécurisée.
     * Par default ne fait rien mais est
     *
     * @see javax.naming.directory.DirContext#search(Name, String, SearchControls)
     */
    protected NamingEnumeration<SearchResult> safeSearch(String base, String filter, SearchControls searchControls) throws NamingException {
        return this.context.search(base, filter, searchControls);
    }


    /////////////////////////// Méthodes de requete AD ///////////////////////////

    //  api/search/person   api/search/structures   api/admin/group/all
    // recherche une personne ou une structure
    // consulter la liste des groupes
    protected NamingEnumeration<SearchResult> search(ObjectType searchType, String searchValue) {
        String filter;
        if (searchValue.equals("*") || searchValue.equals("")) {
            filter = "(&(objectClass=" + searchType + ")(!(cn=Dummy Query)))";
        } else {
            searchValue = searchValue.replaceAll("(?<=\\w)(?=\\w)", "*");
            filter = "(&(objectClass=" + searchType + ")(" + searchType.getNamingAttribute() + "=*" + searchValue + "*)(!(cn=Dummy Query)))";
        }
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = safeSearch(AD_BASE, filter, searchControls);
            return res;
        } catch (NamingException e) {
            log.error("Erreur lors de la recherche", e);
            throw new SeinkSansDoozeBadRequest();
        }
    }

    // api/admin/group/all
    public NamingEnumeration<SearchResult> searchAllGroups() {
        String filter = "(objectClass=group)";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_GROUP_BASE, filter, searchControls);
            return res;
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
        }
    }


    //  api/admin/group/remove/{groupName}/{username}


    //  api/admin/group/members/{groupName}
    public ArrayList<SearchResult> queryGroupMembers(String groupName) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = safeSearch(AD_GROUP_BASE, filter, searchControls);
        } catch (NamingException ex) {
            throw new SeinkSansDoozeBadRequest();
        }
        try {
            if (res.hasMore()) {
                Attributes groupAttributs = res.next().getAttributes();
                Attribute groupMembersName = groupAttributs.get("member");
                ArrayList<SearchResult> groupMembers = new ArrayList<>();
                NamingEnumeration<?> membersList;
                try {
                    membersList = groupMembersName.getAll();
                } catch (NullPointerException e) {
                    return groupMembers;
                }
                while (membersList.hasMore()) {
                    String currentMemberCN = membersList.next().toString();
                    NamingEnumeration<SearchResult> currentMemberEnum = search(ObjectType.PERSON, currentMemberCN.split(",")[0].split("=")[1]);
                    if (currentMemberEnum.hasMore()) {
                        SearchResult currentMember = currentMemberEnum.next();
                        groupMembers.add(currentMember);
                    }
                }
                return groupMembers;
            } else {
                throw new SeinkSansDoozeGroupNotFound();
            }
        } catch (NamingException ex) {
            throw new SeinkSansDoozeGroupNotFound();
        }
    }


    private NamingEnumeration<SearchResult> getStructureChildren(ObjectType type, String structureDN) {
        String filter = "objectClass=" + type;
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = safeSearch(structureDN, filter, searchControls);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
        }
        return res;
    }

    protected List<PartialPerson> getStructureMember(String structureDN) {
        NamingEnumeration<SearchResult> res = getStructureChildren(ObjectType.PERSON, structureDN);
        List<PartialPerson> members = new ArrayList<>();
        try {
            while (res.hasMore()) {
                SearchResult currentMember = res.next();
                PartialPerson member = new PartialPerson(currentMember);
                members.add(member);
            }
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
        }
        return members;
    }

    protected List<PartialStructure> getStructureSubStructures(String structureDN) {
        NamingEnumeration<SearchResult> res = getStructureChildren(ObjectType.STRUCTURE, structureDN);
        List<PartialStructure> children = new ArrayList<>();
        try {
            while (res.hasMore()) {
                SearchResult currentChild = res.next();
                PartialStructure child = new PartialStructure(currentChild);
                children.add(child);
            }
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
        }
        return children;
    }
}
