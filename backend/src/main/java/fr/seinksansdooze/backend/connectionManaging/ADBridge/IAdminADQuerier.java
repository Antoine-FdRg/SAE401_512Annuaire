package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

public interface IAdminADQuerier {
    boolean login(String username, String pwd);

    boolean logout();

    NamingEnumeration<SearchResult> searchAllGroups();

    boolean createGroup(String groupName);

    boolean deleteGroup(String groupName);

    boolean addUserToGroup(String groupName, String username);

    boolean removeUserFromGroup(String groupName, String username);

    NamingEnumeration<SearchResult> searchPerson(String person, String groupName);
}
