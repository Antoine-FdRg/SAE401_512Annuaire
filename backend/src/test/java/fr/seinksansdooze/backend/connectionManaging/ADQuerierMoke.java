package fr.seinksansdooze.backend.connectionManaging;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IPublicADQuerier;
import fr.seinksansdooze.backend.model.payload.NewPersonPayload;
import fr.seinksansdooze.backend.model.response.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ADQuerierMoke implements IAuthentifiedADQuerier, IPublicADQuerier {

    private static final HashMap<String, String> user = new HashMap<>(){{
        put("user1", "mdp1");
    }};


    @Override
    public LoggedInUser login(String username, String pwd) {
        if (user.containsKey(username) && user.get(username).equals(pwd)){
            return new LoggedInUser();
        }
        return null;
    }

    @Override
    public void logout() {

    }

    @Override
    public void createPerson(NewPersonPayload person) {

    }

    @Override
    public void deletePerson(String dn) {

    }

    @Override
    public ArrayList<PartialGroup> getAllGroups() {
        return null;
    }

    @Override
    public List<PartialPerson> searchPerson(String cn, int page, int perPage) {
        return null;
    }

    @Override
    public List<PartialStructure> searchStructure(String ou, int page, int perPage) {
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
    public ArrayList<PartialPerson> getGroupMembers(String groupName) {
        return null;
    }

    @Override
    public boolean addUserToGroup(String userDN, String groupName) {
        return false;
    }

    @Override
    public boolean removeUserFromGroup(String dn, String groupName) {
        return false;
    }

    @Override
    public FullPerson getFullPersonInfo(String dn) {
        return null;
    }

    @Override
    public boolean changePassword(String cn, String prevPwd, String newPwd) {
        return false;
    }

    @Override
    public FullStructure getStructureInfo(String dn) {
        return null;
    }

    @Override
    public List<PartialPerson> searchPerson(String search, String filter, String value, int page, int perPage) {
        return null;
    }

    @Override
    public List<String> getAllFilters() {
        return null;
    }

    @Override
    public void modifyAttribute(String attribute, String value) {

    }
}
