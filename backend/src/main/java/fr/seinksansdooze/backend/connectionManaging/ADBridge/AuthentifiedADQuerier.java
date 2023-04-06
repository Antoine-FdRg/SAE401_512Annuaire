package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.model.ObjectType;
import fr.seinksansdooze.backend.model.exception.*;
import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBadRequest;
import fr.seinksansdooze.backend.model.exception.group.SeinkSansDoozeGroupNotFound;
import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserNotFound;
import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserNotInGroup;
import fr.seinksansdooze.backend.model.response.FullPerson;
import fr.seinksansdooze.backend.model.response.PartialGroup;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.http.HttpStatus;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AuthentifiedADQuerier extends ADQuerier implements IAuthentifiedADQuerier {

    public AuthentifiedADQuerier(String username, String pwd) {
        super(username, pwd);
    }

    public AuthentifiedADQuerier() {
    }

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
                throw new SeinkSansDoozeBackException(HttpStatus.CONFLICT, "Le groupe existe déjà");
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
            throw new SeinkSansDoozeBadRequest();
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
                throw new SeinkSansDoozeGroupNotFound();
            }
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
        }

    }

    @Override
    public ArrayList<PartialPerson> getGroupMembers(String groupName) {
        ArrayList<SearchResult> res = this.queryGroupMembers(groupName);
        ArrayList<PartialPerson> members = new ArrayList<>();
        for (SearchResult currentMember : res) {
            PartialPerson member = new PartialPerson(currentMember);
            members.add(member);
        }
        return members;
    }
    @Override
    public boolean addUserToGroup(String username , String groupName) {
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
                try{
                    this.context.modifyAttributes("CN=" + groupName + ","+ADQuerier.AD_BASE, modificationItems);
                    return true;
                }catch(NamingException e){
                    throw new SeinkSansDoozeUserNotInGroup();
                }
            } else {
                throw new SeinkSansDoozeGroupNotFound();
            }
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();

        }
    }

    @Override
    public boolean removeUserFromGroup(String username , String groupName) {
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
                try{
                    this.context.modifyAttributes("CN=" + groupName + ","+ADQuerier.AD_BASE, modificationItems);

                }
                catch(NamingException e){
                    throw new SeinkSansDoozeUserNotInGroup();
                }
                return true;
            } else {
                throw new SeinkSansDoozeGroupNotFound();

            }
        } catch (NamingException e) {

            throw new SeinkSansDoozeBadRequest();
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
                return new FullPerson(currentPerson);
            }
            throw new SeinkSansDoozeUserNotFound();
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
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

                String encodedPwd;
                try {
                    encodedPwd = DatatypeConverter.printBase64Binary(('"'+"Jfi8ZH8#k"+'"').getBytes("UTF-16LE"));
                } catch (UnsupportedEncodingException e) {
                    throw new SeinkSansDoozeBackException(HttpStatus.NOT_ACCEPTABLE,"Erreur lors de l'encodage du mot de passe");
                }
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", encodedPwd));
                this.context.modifyAttributes(dn, mods);
                return true;
            }
            throw new SeinkSansDoozeUserNotFound();
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
        }
    }

    public List<PartialPerson> searchPerson(String search, String filter, String value, int page, int perPage){
        NamingEnumeration<SearchResult> res = search(ObjectType.PERSON,search,filter,value);
        return (List<PartialPerson>) unroll(res, page, perPage, PartialPerson.class);
    }

    @Override
    public List<PartialPerson> getStructureInfo(String cn) {
        //TODO apres avoir ajouter les uid pour chaque object
        return List.of();
    }

}
