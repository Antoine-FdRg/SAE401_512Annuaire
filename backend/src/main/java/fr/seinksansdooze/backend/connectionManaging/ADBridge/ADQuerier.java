package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.model.response.PartialPerson;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class ADQuerier implements IAdminADQuerier, IPublicADQuerier {
    private static final String AD_URL = "ldap://10.22.32.2:389";
    private static final String AD_BASE = "OU=512BankFR,DC=EQUIPE1B,DC=local";

    private DirContext context;

    private static IPublicADQuerier publicADQuerier = null;

    public static IPublicADQuerier getPublicADQuerier() {
        if (publicADQuerier == null) {
            publicADQuerier = new ADQuerier("antoine.fadda.rodriguez","@Arnaudisthebest83");
        }
        return publicADQuerier;
    }

    private ADQuerier(String username, String pwd) {
        //TODO : comment créer une connexion public  de sort à pouvoir avoir un utilisateur connecté à la bdd pour des requetes publiques et un autre authentifié pour des requetes admin
        //rajouter un systeme de session et d'authentification
        boolean connected = this.login(username, pwd);
    }

    //  api/admin/login
    public boolean login(String username, String pwd) {
        Properties env = new Properties();
        username = username + "@EQUIPE1B.local";
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, pwd);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, AD_URL); //389 (search et createSubcontext) ou 3268 (search only)
        try {
            this.context = new InitialDirContext(env);
            return true;
        } catch (NamingException e) {
            return false;
        }
    }

    private boolean isAdminConnected(String username) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String filter = "(&(objectClass=user)(userPrincipalName=" + username + ")(memberOf=CN=Administrateurs,CN=Builtin,DC=EQUIPE1B,DC=local))";
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            return res.hasMore();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    //  api/admin/logout
    public boolean logout() {
        try {
            this.context.close();
            return true;
        } catch (NamingException e) {
            return false;
        }
    }

    public ArrayList<PartialPerson> searchPerson(String searchedName){
        NamingEnumeration<SearchResult> res = this.search(ObjectType.PERSON, searchedName);
        ArrayList<PartialPerson> persons = new ArrayList<>();
        try {
            while (res.hasMore()) {
                SearchResult currentPerson = res.next();
                Attributes attrs = currentPerson.getAttributes();
                PartialPerson person = new PartialPerson();
                person.setCn(attrs.get("cn").get().toString());
                person.setFirstName(attrs.get("givenName").get().toString());
                person.setLastName(attrs.get("sn").get().toString());
                person.setStructureOU(attrs.get("distinguishedName").get().toString().split(",")[1].split("=")[1]);
                persons.add(person);
            }
            return persons;
        }catch (NamingException e) {
//            throw new RuntimeException(e);
            return persons;
        }
    }







    //  api/search/person   api/search/structures   api/admin/group/all
    // recherche une personne ou une structure
    // consulter la liste des groupes
    // TODO séparer en plusieurs méthodes pour limiter l'accès grace aux interfaces
    // TODO rajouter un paramètre filtrer pour permettre les requetes incluant des filtres
    private NamingEnumeration<SearchResult> search(ObjectType searchType, String searchValue) {
        String filter = "(&(objectClass=" + searchType + ")(" + searchType.getNamingAttribute() + "=*" + searchValue + "*))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            return res;
        } catch (NamingException e) {
//            throw new RuntimeException(e);
            return null;
        }
    }


    //  api/info/person/{cn}
    public NamingEnumeration<SearchResult> getPersonInfo(String cn) {
        String filter = "(&(objectClass=user)(CN=" + cn + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            return res;
        } catch (NamingException e) {
            return null;
        }
    }


    //  api/info/structure/{ou}
    public NamingEnumeration<SearchResult> searchStructure(String ou) {
        String filter = "(&(objectClass=organizationalUnit)(distinguishedName=" + ou + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            return res;
        } catch (NamingException e) {
            return null;
        }
    }

    // api/admin/group/all
    public NamingEnumeration<SearchResult> searchAllGroups() {
        String filter = "(&(objectClass=group))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            return res;
        } catch (NamingException e) {
            return null;
        }
    }

    //  api/admin/group/create/{groupName}
    public boolean createGroup(String groupName) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                return false;
            } else {
                Attributes attributes = new BasicAttributes();
                Attribute objectClass = new BasicAttribute("objectClass");
                objectClass.add("top");
                objectClass.add("group");
                attributes.put(objectClass);
                attributes.put("CN", groupName);
                attributes.put("sAMAccountName", groupName);
                attributes.put("description", "Groupe créé par l'API");
                this.context.createSubcontext("CN=" + groupName + ","+ADQuerier.AD_BASE, attributes);
                return true;
            }
        } catch (NamingException e) {
            return false;
        }
    }

    //  api/admin/group/delete/{groupName}
    public boolean deleteGroup(String groupName) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                this.context.destroySubcontext("CN=" + groupName + ","+ADQuerier.AD_BASE);
                return true;
            } else {
                return false;
            }
        } catch (NamingException e) {
            return false;
        }
    }

    //  api/admin/group/add/{groupName}/{username}
    public boolean addUserToGroup(String groupName, String username) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                ModificationItem[] modificationItems = new ModificationItem[1];
                String dn = search(ObjectType.PERSON,username).next().getNameInNamespace();
                modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member", dn));
                this.context.modifyAttributes("CN=" + groupName + ","+ADQuerier.AD_BASE, modificationItems);
                return true;
            } else {
                return false;
            }
        } catch (NamingException e) {
            return false;
        }
    }

    //  api/admin/group/remove/{groupName}/{username}
    public boolean removeUserFromGroup(String groupName, String username) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                ModificationItem[] modificationItems = new ModificationItem[1];
                String dn = search(ObjectType.PERSON,username).next().getNameInNamespace();
                modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("member", dn));
                this.context.modifyAttributes("CN=" + groupName + ","+ADQuerier.AD_BASE, modificationItems);
                return true;
            } else {
                return false;
            }
        } catch (NamingException e) {
            return false;
        }
    }

    //  api/admin/group/members/{groupName}
    public ArrayList<SearchResult> searchGroupMembers(String groupName) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            try {
                res = this.context.search(AD_BASE, filter, searchControls);
            } catch (NamingException ex) {
                throw new RuntimeException(ex);
            }
//            System.out.println("nom du group trouvé : ");
//            TestADQuerier.displayResults(res,ObjectType.GROUP);
            //le group test est bien trouvé
            if (res.hasMore()) {
                Attributes groupAttributs = res.next().getAttributes();
                Attribute groupMembersName = groupAttributs.get("member");
                ArrayList<SearchResult> groupMembers = new ArrayList<>();
                NamingEnumeration<?> membersList = groupMembersName.getAll();
                while (membersList.hasMore()) {
                    String currentMemberCN = membersList.next().toString();
                    NamingEnumeration<SearchResult> currentMemberEnum = search(ObjectType.PERSON,currentMemberCN.split(",")[0].split("=")[1]);
                    if(currentMemberEnum.hasMore()){
                        SearchResult currentMember = currentMemberEnum.next();
                        groupMembers.add(currentMember);
                    }
                }
                return groupMembers;
            }else {
                return null;
            }
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    //  api/admin/search/person/{person}/{groupName}
    public NamingEnumeration<SearchResult> searchPerson(String person, String groupName) {
        String filter = "(&(objectClass=user)(CN=*" + person + "*))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                return res;
            } else {
                return null;
            }
        } catch (NamingException e) {
            return null;
        }
    }
}
