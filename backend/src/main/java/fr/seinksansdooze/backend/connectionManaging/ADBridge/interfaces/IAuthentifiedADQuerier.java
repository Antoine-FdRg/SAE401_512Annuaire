package fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces;

import fr.seinksansdooze.backend.model.response.FullPerson;
import fr.seinksansdooze.backend.model.response.PartialGroup;
import fr.seinksansdooze.backend.model.response.PartialPerson;

import java.util.ArrayList;

public interface IAuthentifiedADQuerier {
    boolean login(String username, String pwd);

    boolean logout();

    ArrayList<PartialGroup> getAllGroups();

    boolean createGroup(String groupName);

    boolean deleteGroup(String groupName);

    ArrayList<PartialPerson> getGroupMembers(String groupName);

    boolean addUserToGroup(String username , String groupName);

    boolean removeUserFromGroup(String groupName, String username);

    FullPerson getFullPersonInfo(String cn);

    boolean changePassword(String cn, String prevPwd,String newPwd);

}
