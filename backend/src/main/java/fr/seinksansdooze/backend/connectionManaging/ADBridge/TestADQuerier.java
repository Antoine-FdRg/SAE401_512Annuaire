package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IPublicADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.model.ObjectType;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;

public class TestADQuerier {

    public static void main(String[] args) {
        testPublicQuerier();
        testAuthenticatedQuerier();

//

//        System.out.println("api/info/person/{cn}");
//        System.out.println(adQuerier.getPersonInfo("Thomas TG. Gorisse"));

//        System.out.println("api/info/structure/{ou}");
//        System.out.println(adQuerier.getStructureInfo("OU=Direction Général,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));

//        System.out.println("api/admin/group/all");
//        results = adQuerier.searchAllGroups();
//        displayResults(results, ObjectType.GROUP);

//        System.out.println("api/admin/group/create/{groupName}");
//        if (adQuerier.createGroup("test")) {
//            System.out.println("Group created");
//        } else {
//            System.out.println("Group not created");
//        }
//
//        System.out.println("api/admin/group/delete/{groupName}");
//        if(adQuerier.deleteGroup("test")){
//            System.out.println("Group deleted");
//        } else {
//            System.out.println("Group not deleted");
//        }

//        System.out.println("api/admin/group/add/{groupName}/{cn}");
//        if (adQuerier.addUserToGroup("test", "Clément CL. Lefèvre")) {
//            System.out.println("User added to group");
//        } else {
//            System.out.println("User not added to group");
//        }

//        System.out.println("api/admin/group/remove/{groupName}/{username}");
//        if(adQuerier.removeUserFromGroup("test", "Thomas TG. Gorisse")){
//            System.out.println("User removed from group");
//        } else {
//            System.out.println("User not removed from group");
//        }

//        System.out.println("api/admin/search/person/{person}/{groupName}");
//        results = adQuerier.searchPerson("Thomas TG. Gorisse", "test");
//        displayResults(results, ObjectType.GROUP);

        //api/admin/group/members/{groupName}
//        System.out.println("api/admin/group/members/{groupName}");
//        ArrayList<SearchResult> ALresults = adQuerier.searchGroupMembers("test");
//        displayResults(ALresults, ObjectType.GROUP);

//        System.out.println("CN=Clément CL. Lefèvre,OU=Direction Général Adj,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local".split(",")[0].split("=")[1]);

//          adQuerier.searchPerson("clément").forEach(System.out::println);
//          adQuerier.searchStructure("Direction").forEach(System.out::println);

//            adQuerier.getAllGroups().forEach(System.out::println);
    }

    private static void testAuthenticatedQuerier() {
        String id = "antoine.fadda.rodriguez";
        String pwd = "@Arnaudisthebest83";

        String id2 = "dummy.query";
        String pwd2 = "@Azertyuiop06200";

        String id3 = "thomas.gorisse";
        String pwd3 = "@Arnaudisthebest83";

        IAuthentifiedADQuerier querier = new AuthentifiedADQuerier(id3, pwd3);
        querier.getAllGroups().forEach(System.out::println);
        System.out.println(querier.createGroup("test1"));
        System.out.println(querier.getFullPersonInfo("Thomas TG. Gorisse"));
    }

    private static void testPublicQuerier(){
        IPublicADQuerier adQuerier = new PublicADQuerier("dummy.query","@Azertyuiop06200");

        adQuerier.searchPerson("thomas").forEach(System.out::println);
        adQuerier.searchStructure("direc").forEach(System.out::println);
        System.out.println(adQuerier.getPartialPersonInfo("Thomas TG. Gorisse"));
        System.out.println(adQuerier.getPartialStructureInfo("OU=Direction Général,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));

        ((ADQuerier) adQuerier).logout();
    }

}
