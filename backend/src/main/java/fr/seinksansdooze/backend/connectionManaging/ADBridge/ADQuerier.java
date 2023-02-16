package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Properties;

public class ADQuerier implements IAdminADQuerier {
    private static final String AD_URL = "ldap://10.22.32.2:389";
    private static final String AD_BASE = "OU=512BankFR,DC=EQUIPE1B,DC=local";

    private DirContext context;

    public ADQuerier() {
        //TODO : comment créer une connexion public  de sort à pouvoir avoir un utilisateur connecté à la bdd pour des requetes publiques et un autre authentifié pour des requetes admin
        //rajouter un systeme de session et d'authentification
        boolean connected = this.login("antoine.fadda.rodriguez", "@Arnaudisthebest83");
    }

    //  api/admin/login
    public boolean login(String username, String pwd) {
        Properties env = new Properties();
        username = username + "@EQUIPE1B.local";
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, pwd);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.22.32.2:389"); //389 (search et createSubcontext) ou 3268 (search only)
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

    //  api/search/person   api/search/structures   api/admin/group/all
    public NamingEnumeration<SearchResult> search(ObjectType searchType, String searchValue) {
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
    public NamingEnumeration<SearchResult> searchPerson(String cn) {
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
                this.context.createSubcontext("CN=" + groupName + ",OU=512BankFR,DC=EQUIPE1B,DC=local", attributes);
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
                this.context.destroySubcontext("CN=" + groupName + ",OU=512BankFR,DC=EQUIPE1B,DC=local");
                return true;
            } else {
                return false;
            }
        } catch (NamingException e) {
            return false;
        }
    }

    //  api/admin/group/add/{groupName}/{username}
    //TODO : a tester
    public boolean addUserToGroup(String groupName, String username) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                ModificationItem[] modificationItems = new ModificationItem[1];
                String dn = searchPerson(username).next().getNameInNamespace();
                modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member", dn));
                this.context.modifyAttributes("CN=" + groupName + ",OU=512BankFR,DC=EQUIPE1B,DC=local", modificationItems);
                return true;
            } else {
                return false;
            }
        } catch (NamingException e) {
            return false;
        }
    }

    //  api/admin/group/remove/{groupName}/{username}
    //TODO : A tester
    public boolean removeUserFromGroup(String groupName, String username) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                ModificationItem[] modificationItems = new ModificationItem[1];
                String dn = searchPerson(username).next().getNameInNamespace();
                modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("member", dn));
                this.context.modifyAttributes("CN=" + groupName + ",OU=512BankFR,DC=EQUIPE1B,DC=local", modificationItems);
                return true;
            } else {
                return false;
            }
        } catch (NamingException e) {
            return false;
        }
    }

    //  api/admin/group/members/{groupName}
    //TODO : A tester
    private NamingEnumeration<SearchResult> searchGroupMembers(String groupName) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                Attributes attributes = res.next().getAttributes();
                Attribute members = attributes.get("member");
                NamingEnumeration<?> membersList = members.getAll();
                String filter2 = "(&(objectClass=user)(CN=" + membersList.next().toString() + "))";
                SearchControls searchControls2 = new SearchControls();
                searchControls2.setSearchScope(SearchControls.SUBTREE_SCOPE);
                NamingEnumeration<SearchResult> res2;
                try {
                    res2 = this.context.search(AD_BASE, filter2, searchControls2);
                    return res2;
                } catch (NamingException e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (NamingException e) {
            return null;
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
