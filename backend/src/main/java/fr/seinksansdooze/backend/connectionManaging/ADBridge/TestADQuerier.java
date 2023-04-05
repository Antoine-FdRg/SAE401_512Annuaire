package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IPublicADQuerier;
public class TestADQuerier {

    public static void main(String[] args) {
//        testPublicQuerier();
//        testAuthenticatedQuerier();

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

        IAuthentifiedADQuerier querier = new AuthentifiedADQuerier(id, pwd);
        querier.getAllGroups().forEach(System.out::println);
        System.out.println("Récupération des infos de Thomas");
        System.out.println(querier.getFullPersonInfo("Thomas TG. Gorisse"));
        System.out.println("Ajout de Thomas au groupe test");
        System.out.println(querier.addUserToGroup( "Thomas TG. Gorisse", "test"));
        System.out.println("Affichage des membres du groupe test");
        querier.getGroupMembers("test").forEach(System.out::println);
        System.out.println("Suppression de Thomas du groupe test");
        System.out.println(querier.removeUserFromGroup("Thomas TG. Gorisse", "test"));
        System.out.println("Affichage des membres du groupe test");
        querier.getGroupMembers("test").forEach(System.out::println);



    }

    private static void testPublicQuerier(){
        IPublicADQuerier adQuerier = new PublicADQuerier("dummy.query","@Azertyuiop06200");

        adQuerier.searchPerson("thomas", 0, 25).forEach(System.out::println);
        adQuerier.searchStructure("direc", 0, 25).forEach(System.out::println);
        System.out.println(adQuerier.getPartialPersonInfo("Thomas TG. Gorisse"));
        System.out.println(adQuerier.getPartialStructureInfo("OU=Direction Général,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));

        ((ADQuerier) adQuerier).logout();
    }

}
