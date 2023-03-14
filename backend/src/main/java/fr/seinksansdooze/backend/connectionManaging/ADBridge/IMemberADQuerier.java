package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.model.response.PartialGroup;
import fr.seinksansdooze.backend.model.response.PartialPerson;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;

public interface IMemberADQuerier extends IPublicADQuerier{
    boolean login(String username, String pwd);

    boolean logout();

    ArrayList<PartialGroup> getAllGroups();

    boolean createGroup(String groupName);

    boolean deleteGroup(String groupName);

    boolean addUserToGroup(String groupName, String username);

    boolean removeUserFromGroup(String groupName, String username);

    NamingEnumeration<SearchResult> searchPerson(String person, String groupName);

    PartialPerson getFullPersonInfo(String cn);


}
