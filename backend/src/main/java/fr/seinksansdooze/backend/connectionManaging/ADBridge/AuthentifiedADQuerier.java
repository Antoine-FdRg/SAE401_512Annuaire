package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.model.ADFilter;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.model.ObjectType;
import fr.seinksansdooze.backend.model.exception.*;
import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBadRequest;
import fr.seinksansdooze.backend.model.exception.group.SeinkSansDoozeGroupNotFound;
import fr.seinksansdooze.backend.model.exception.structure.SeinkSansDoozeStructureNotFound;
import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserAlreadyExists;
import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserAlreadyInGroup;
import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserNotFound;
import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserNotInGroup;
import fr.seinksansdooze.backend.model.payload.NewPersonPayload;
import fr.seinksansdooze.backend.model.response.*;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.http.HttpStatus;

import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AuthentifiedADQuerier extends ADQuerier implements IAuthentifiedADQuerier {

    public AuthentifiedADQuerier(String username, String pwd) {
        super(username, pwd);
    }

    public AuthentifiedADQuerier() {
    }

    private LoggedInUser loggedInUser;

    @Override
    public LoggedInUser login(String username, String pwd) {
        this.loggedInUser = super.login(username, pwd);
        return this.loggedInUser;
    }

    @Override
    public void createPerson(NewPersonPayload person) {
        Attributes attributes = new BasicAttributes();
        Attribute objectClass = new BasicAttribute("objectClass");
        objectClass.add("top");
        objectClass.add("person");
        objectClass.add("organizationalPerson");
        objectClass.add("user");
        attributes.put(objectClass);
        attributes.put("givenName", person.getFirstName());
        attributes.put("sn", person.getLastName());
        attributes.put("title", person.getTitle());

        attributes.put("sAMAccountName", person.getLastName().split(" ")[0].toLowerCase() + "." + person.getFirstName().toLowerCase());
        attributes.put("mail", person.getLastName().split(" ")[0].toLowerCase() + "." + person.getFirstName().toLowerCase() + "@equipe1B.local");
        attributes.put("mobile", person.getPersonalPhone());
        attributes.put("telephoneNumber", person.getProfessionalPhone());
        attributes.put("streetAddress", person.getAddress());
        attributes.put("dateDeNaissance", person.getDateOfBirth());
        attributes.put("manager", person.getManagerDN());
        try {
            this.context.createSubcontext("CN=" + person.getFirstName() + " " + person.getLastName() + "," + person.getStructureDN(), attributes);
        } catch (NameAlreadyBoundException e) {
            throw new SeinkSansDoozeUserAlreadyExists();
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
        }
    }

    @Override
    public void deletePerson(String dn) {
        if(Objects.isNull(dn) || !dn.contains("CN="))
            throw new SeinkSansDoozeBadRequest();
        if(this.getFullPersonInfo(dn).getClass() != FullPerson.class)
            throw new SeinkSansDoozeUserNotFound();
        try {
            this.context.destroySubcontext(dn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeUserNotFound();
        }
    }

    /**
     * Méthode répondant à la route GET /api/admin/group/all
     *
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
        } catch (NamingException e) {
            return groups;
        }
    }

    @Override
    public boolean createGroup(String groupName) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        List<PartialGroup> existingGroups;
        try {
            existingGroups = this.getAllGroups();
            if (existingGroups.stream().anyMatch(group -> group.getCn().equals(groupName))) {
                throw new SeinkSansDoozeBackException(HttpStatus.CONFLICT, "Le groupe existe déjà.");
            } else {
                Attributes attributes = new BasicAttributes();
                Attribute objectClass = new BasicAttribute("objectClass");
                objectClass.add("top");
                objectClass.add("group");
                attributes.put(objectClass);
                attributes.put("CN", groupName);
                attributes.put("sAMAccountName", groupName);
                attributes.put("description", "Groupe créé par l'API");
                this.context.createSubcontext("CN=" + groupName + "," + ADQuerier.AD_GROUP_BASE, attributes);
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
            res = this.context.search(AD_GROUP_BASE, filter, searchControls);
            if (res.hasMore()) {
                this.context.destroySubcontext("CN=" + groupName + "," + ADQuerier.AD_GROUP_BASE);
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
    public boolean addUserToGroup(String userDN, String groupName) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_GROUP_BASE, filter, searchControls);
            if (res.hasMore()) {
                ModificationItem[] modificationItems = new ModificationItem[1];
                if (this.getFullPersonInfo(userDN) == null) {
                    throw new SeinkSansDoozeUserNotFound();
                }
                modificationItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member", userDN));
                try {
                    this.context.modifyAttributes("CN=" + groupName + "," + ADQuerier.AD_GROUP_BASE, modificationItems);
                    return true;
                } catch (NamingException e) {
                    throw new SeinkSansDoozeUserAlreadyInGroup();
                }
            } else {
                throw new SeinkSansDoozeGroupNotFound();
            }
        } catch (
                NamingException e) {
            throw new SeinkSansDoozeBadRequest();

        }

    }

    @Override
    public boolean removeUserFromGroup(String dn, String groupName) {
        String filter = "(&(objectClass=group)(CN=" + groupName + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;

        try {
            res = this.context.search(AD_GROUP_BASE, filter, searchControls);
            if (res.hasMore()) {
                ModificationItem[] modificationItems = new ModificationItem[1];
                if (this.getFullPersonInfo(dn) == null) {
                    throw new SeinkSansDoozeUserNotFound();
                }
                modificationItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("member", dn));
                try {
                    this.context.modifyAttributes("CN=" + groupName + "," + ADQuerier.AD_GROUP_BASE, modificationItems);
                } catch (NamingException e) {
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
     *
     * @return la liste de tous les groupes de l'annuaire
     */
    @Override
    public FullPerson getFullPersonInfo(String dn) {
        String filter = "(&(objectClass=user)(distinguishedName=" + dn + "))";
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

    public boolean changePassword(String cn, String prevPwd, String newPwd) {
        String filter = "(&(objectClass=user)(CN=" + cn + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                SearchResult currentPerson = res.next();
                String dn = currentPerson.getNameInNamespace();
                this.context.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
                this.context.addToEnvironment(Context.SECURITY_CREDENTIALS, prevPwd);
                ModificationItem[] mods = new ModificationItem[1];

                String encodedPwd;
                try {
                    encodedPwd = DatatypeConverter.printBase64Binary(('"' + "Jfi8ZH8#k" + '"').getBytes("UTF-16LE"));
                } catch (UnsupportedEncodingException e) {
                    throw new SeinkSansDoozeBackException(HttpStatus.NOT_ACCEPTABLE, "Erreur lors de l'encodage du mot de passe");
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

    public List<PartialPerson> searchPerson(String search, String rawFilter, String value, int page, int perPage) {
        String filterAttribute = ADFilter.getFilter(rawFilter);
        if (filterAttribute == null) {
            throw new SeinkSansDoozeBadRequest();
        }
        NamingEnumeration<SearchResult> res = this.search(ObjectType.PERSON, search);
        List<PartialPerson> partialPersons = new ArrayList<>();
        try {
            while (res.hasMore()) {
                SearchResult currentPerson = res.next();
                PartialPerson partialPerson = new PartialPerson(currentPerson);
                partialPersons.add(partialPerson);
            }
        } catch (NamingException e) {
            throw new SeinkSansDoozeUserNotFound();
        }
        List<FullPerson> fullPersonList = partialPersons.stream().map(partialPerson -> this.getFullPersonInfo(partialPerson.getDn())).toList();
        List<FullPerson> filteredFullPersonList = fullPersonList.stream().filter(fullPerson -> fullPerson.check(filterAttribute, value)).toList();
        return filteredFullPersonList.stream().map(FullPerson::toPartialPerson).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllFilters() {
        return ADFilter.getAllFilters();
    }

    @Override
    public void modifyAttribute(String attribute, String value) {
        String filter = "(&(objectClass=user)(distinguishedName=" + this.loggedInUser.getDn() + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                SearchResult currentPerson = res.next();
                ModificationItem[] modificationItems = new ModificationItem[1];
                modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attribute, value));
                try {
                    this.context.modifyAttributes(currentPerson.getNameInNamespace(), modificationItems);
                } catch (NamingException e) {
                    throw new SeinkSansDoozeBadRequest();
                }
            } else {
                throw new SeinkSansDoozeUserNotFound();
            }
        } catch (NamingException e) {
            throw new SeinkSansDoozeBadRequest();
        }
    }

    @Override
    public FullPerson getInfo() {
        return this.getFullPersonInfo(this.loggedInUser.getDn());
    }

    @Override
    public FullStructure getStructureInfo(String dn) {
        String filter = "(&(objectClass=" + ObjectType.STRUCTURE + ")(distinguishedName=" + dn + "))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> res;
        String fsDN = "";
        try {
            res = this.context.search(AD_BASE, filter, searchControls);
            if (res.hasMore()) {
                SearchResult sr = res.next();
                fsDN = sr.getAttributes().get("distinguishedName").get().toString();
            }
        } catch (NamingException e) {
            throw new SeinkSansDoozeStructureNotFound();
        }
        assert !Objects.equals(fsDN, "");
        List<PartialPerson> fsMembers = this.getStructureMember(fsDN);
        List<PartialStructure> fsSubStructures = this.getStructureSubStructures(fsDN);
        return new FullStructure(fsDN, fsSubStructures, fsMembers);
    }

}
