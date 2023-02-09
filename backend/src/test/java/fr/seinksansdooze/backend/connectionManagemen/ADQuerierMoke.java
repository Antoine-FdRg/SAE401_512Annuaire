package fr.seinksansdooze.backend.connectionManagemen;

import fr.seinksansdooze.backend.ADBridge.IADQuerier;
import fr.seinksansdooze.backend.ADBridge.ObjectType;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;

public class ADQuerierMoke implements IADQuerier {

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
    public NamingEnumeration<SearchResult> search(ObjectType searchType, String searchValue) {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> searchPerson(String cn) {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> searchStructure(String ou) {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> searchAllGroups() {
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
}
