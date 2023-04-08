package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IPublicADQuerier;
public class TestADQuerier {

    public static void main(String[] args) {
        testPublicQuerier();
        System.out.println("====================================");
        testAuthenticatedQuerier();
    }

    private static void testAuthenticatedQuerier() {
        String id = "fadda.antoine";
        String pwd = "@Arnaudisthebest83";
        IAuthentifiedADQuerier querier = new AuthentifiedADQuerier(id, pwd);
        String dn = "CN=Thomas Gorisse,OU=Direction General,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local";
//        querier.getAllGroups().forEach(System.out::println);
//        System.out.println("Récupération des infos de Thomas");
        System.out.println(querier.getFullPersonInfo("CN=Antoine Fadda Rodriguez,OU=Secretaire general,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));
//        System.out.println("Création du groupe test");
//        System.out.println(querier.createGroup("test"));


//        System.out.println("Ajout de Thomas au groupe test");
//        System.out.println(querier.addUserToGroup( dn, "test"));
////
//        System.out.println("Affichage des membres du groupe test");
//        querier.getGroupMembers("test").forEach(System.out::println);
//
//        System.out.println("Suppression de Thomas du groupe test");
//        System.out.println(querier.removeUserFromGroup(dn, "test"));
////
//        System.out.println("Affichage des membres du groupe test");
//        querier.getGroupMembers("test").forEach(System.out::println);
////
//        System.out.println("Suppression du groupe test");
//        System.out.println(querier.deleteGroup("test"));

        querier.searchPerson("Antoine","manager","gwendo",0,15).forEach(System.out::println); //streetAddress, title, (postalCode), (manager)

        querier.logout();


    }

    private static void testPublicQuerier(){
        IPublicADQuerier adQuerier = new PublicADQuerier("dummy.query","@Azertyuiop06200");
        System.out.println("recherche de Antoines");
        adQuerier.searchPerson("antoine", 0, 15).forEach(System.out::println);
//        System.out.println("recherche de la direction");
//        adQuerier.searchStructure("direc", 0, 25).forEach(System.out::println);
//        System.out.println("infos de Thomas");
//        System.out.println(adQuerier.getPartialPersonInfo("CN=Thomas Gorisse,OU=Direction General,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));
//        System.out.println("infos de la direction");
//        System.out.println(adQuerier.getPartialStructureInfo("OU=Direction General,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));

        ((ADQuerier) adQuerier).logout();
    }

}
