package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.model.ObjectType;
import fr.seinksansdooze.backend.model.response.FullPerson;
import fr.seinksansdooze.backend.model.response.PartialGroup;
import jakarta.xml.bind.DatatypeConverter;
import lombok.val;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AuthentifiedADQuerier extends ADQuerier implements IAuthentifiedADQuerier {


    /**
     * Méthode répondant à la route GET /api/admin/group/all
     * @return la liste de tous les groupes
     */
    @Override
    public ArrayList<PartialGroup> getAllGroups() {
        NamingEnumeration<SearchResult> res = this.searchAllGroups();
        ArrayList<PartialGroup> groups = new ArrayList<>();
        try {
            while (res.hasMore()) {
                SearchResult currentGroup = res.next();
                PartialGroup group = new PartialGroup(currentGroup);
                groups.add(group);
            }
            return groups;
        }catch (NamingException e) {
            return groups;
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    /**
     * Méthode répondant à la route GET /api/admin/search/person
     * @return la liste de tous les groupes de l'annuaire
     */
    @Override
    public FullPerson getFullPersonInfo(String cn) {
        String filter = "(&(objectClass=user)(CN=" + cn + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                SearchResult currentPerson = res.next();
                FullPerson person = new FullPerson(currentPerson);
                return person;
            }
            return null;
        } catch (NamingException e) {
            return null;
        }
    }

    public boolean changePassword(String cn, String prevPwd,String newPwd){
        String filter = "(&(objectClass=user)(CN=" + cn + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                SearchResult currentPerson = res.next();
                String dn = currentPerson.getNameInNamespace();
                System.out.println(dn);
                this.context.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
                this.context.addToEnvironment(Context.SECURITY_CREDENTIALS, prevPwd);
                ModificationItem[] mods = new ModificationItem[1];
                val encodedPwd = DatatypeConverter.printBase64Binary(('"'+"Jfi8ZH8#k"+'"').getBytes("UTF-16LE"));
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", encodedPwd));
                this.context.modifyAttributes(dn, mods);
                return true;
            }
            return false;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
