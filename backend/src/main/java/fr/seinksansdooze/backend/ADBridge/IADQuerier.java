package fr.seinksansdooze.backend.ADBridge;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

public interface IADQuerier {
    public boolean login(String username, String pwd);
    public boolean logout();
    public NamingEnumeration<SearchResult> search(ObjectType searchType, String searchValue);
    public NamingEnumeration<SearchResult> searchPerson(String cn);
    public NamingEnumeration<SearchResult> searchStructure(String ou);
    public NamingEnumeration<SearchResult> searchAllGroups();
    public boolean createGroup(String groupName);
    public boolean deleteGroup(String groupName);
    public boolean addUserToGroup(String groupName, String username);
    public boolean removeUserFromGroup(String groupName, String username);
    public NamingEnumeration<SearchResult> searchPerson(String person, String groupName);
}
