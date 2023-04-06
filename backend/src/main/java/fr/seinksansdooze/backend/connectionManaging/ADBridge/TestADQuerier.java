package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IPublicADQuerier;
public class TestADQuerier {

    public static void main(String[] args) {
//        testPublicQuerier();
        System.out.println("====================================");
        testAuthenticatedQuerier();
    }

    private static void testAuthenticatedQuerier() {
        String id = "fadda.antoine";
        String pwd = "@Arnaudisthebest83";

        IAuthentifiedADQuerier querier = new AuthentifiedADQuerier(id, pwd);
//        querier.getAllGroups().forEach(System.out::println);
//        System.out.println("Récupération des infos de Thomas");
//        System.out.println(querier.getFullPersonInfo("Gorisse Thomas"));
//        System.out.println("Création du groupe test");
//        System.out.println(querier.createGroup("test"));
//        System.out.println("Ajout de Thomas au groupe test");
//        System.out.println(querier.addUserToGroup( "Thomas TG. Gorisse", "test"));
//        System.out.println("Affichage des membres du groupe test");
//        querier.getGroupMembers("test").forEach(System.out::println);
//        System.out.println("Suppression de Thomas du groupe test");
//        System.out.println(querier.removeUserFromGroup("Thomas TG. Gorisse", "test"));
//        System.out.println("Affichage des membres du groupe test");
//        querier.getGroupMembers("test").forEach(System.out::println);
//        querier.searchPerson("Antoine","title","directeur",0,15).forEach(System.out::println); //streetAddress, title, (postalCode), (postalAdress), (manager)

        querier.logout();


    }

    private static void testPublicQuerier(){
        IPublicADQuerier adQuerier = new PublicADQuerier("dummy.query","@Azertyuiop06200");

        adQuerier.searchPerson("antoine", 0, 25).forEach(System.out::println);
//        adQuerier.searchStructure("direc", 0, 25).forEach(System.out::println);
//        System.out.println(adQuerier.getPartialPersonInfo("Thomas TG. Gorisse"));
//        System.out.println(adQuerier.getPartialStructureInfo("OU=Direction Général,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));

        ((ADQuerier) adQuerier).logout();
    }

}
