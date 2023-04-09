package fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces;

import fr.seinksansdooze.backend.model.response.*;

import java.util.ArrayList;
import java.util.List;

public interface IAuthentifiedADQuerier {
    LoggedInUser login(String username, String pwd);

    void logout();

    ArrayList<PartialGroup> getAllGroups();

    boolean createGroup(String groupName);

    boolean deleteGroup(String groupName);

    ArrayList<PartialPerson> getGroupMembers(String groupName);

    boolean addUserToGroup(String userDN , String groupName);

    boolean removeUserFromGroup(String dn, String groupName);

    FullPerson getFullPersonInfo(String dn);

    boolean changePassword(String cn, String prevPwd,String newPwd);

    FullStructure getStructureInfo(String dn);

    List<PartialPerson> searchPerson(String search, String filter, String value, int page, int perPage);

    List<String> getAllFilters();

    void modifyAttribute(String attribute, String value);
}
