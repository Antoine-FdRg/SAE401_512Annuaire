package fr.seinksansdooze.backend.connectionManaging;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.IMemberADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.IPublicADQuerier;
import fr.seinksansdooze.backend.model.response.PartialGroup;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;

public class ADQuerierMoke implements IMemberADQuerier, IPublicADQuerier {

    private static final HashMap<String, String> user = new HashMap<>(){{
        put("user1", "mdp1");
    }};


    @Override
    public boolean login(String username, String pwd) {
        if (user.containsKey(username)) {
            return user.get(username).equals(pwd);
        }
        return false;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public ArrayList<PartialGroup> getAllGroups() {
        return null;
    }

    @Override
    public ArrayList<PartialPerson> searchPerson(String cn) {
        return null;
    }

    @Override
    public ArrayList<PartialStructure> searchStructure(String ou) {
        return null;
    }

    @Override
    public PartialPerson getPartialPersonInfo(String cn) {
        return null;
    }

    @Override
    public PartialStructure getPartialStructureInfo(String ou) {
        return null;
    }


    @Override
    public boolean createGroup(String groupName) {
        return false;
    }

    @Override
    public boolean deleteGroup(String groupName) {
        return false;
    }

    @Override
    public boolean addUserToGroup(String groupName, String username) {
        return false;
    }

    @Override
    public boolean removeUserFromGroup(String groupName, String username) {
        return false;
    }

    @Override
    public NamingEnumeration<SearchResult> searchPerson(String person, String groupName) {
        return null;
    }

    @Override
    public PartialPerson getFullPersonInfo(String cn) {
        return null;
    }
}
