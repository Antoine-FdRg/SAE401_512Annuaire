package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IAuthentifiedADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IPublicADQuerier;
import fr.seinksansdooze.backend.model.payload.NewPersonPayload;

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
        String dn = "CN=Thomas Gorisse,OU=Direction General,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local";
        String structureDn = "OU=Secteur Comptabilite,OU=Departement Finance,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local";

        System.out.println(querier.getFullPersonInfo(dn));
        NewPersonPayload person = new NewPersonPayload("Donald","Trump",structureDn,"Employé","0611911911","01234567890","19610101","Tour a son nom","CN=Bernard Roger,OU=Secteur Comptabilite,OU=Departement Finance,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local");
//        querier.createPerson(person);
//        System.out.println(querier.getFullPersonInfo("CN=Donald Trump,OU=Secteur Comptabilite,OU=Departement Finance,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));
        querier.deletePerson("CN=Donald Trump,OU=Secteur Comptabilite,OU=Departement Finance,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local");
//        System.out.println(querier.getFullPersonInfo("CN=Donald Trump,OU=Secteur Comptabilite,OU=Departement Finance,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));

        //        querier.modifyAttribute("streetAddress","22 rue de la Chapelle");
//        querier.modifyAttribute("mobile","0696969696");
//        querier.modifyAttribute("telephoneNumber","01234567890");
//        System.out.println(querier.getFullPersonInfo("CN=Antoine Fadda Rodriguez,OU=Secretaire general,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));
        //        System.out.println(querier.getStructureInfo(structureDn));
//        querier.getAllGroups().forEach(System.out::println);
//        System.out.println("Récupération des infos de Thomas");
//        System.out.println(querier.getFullPersonInfo("CN=Antoine Fadda Rodriguez,OU=Secretaire general,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local"));
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

//        querier.searchPerson("Antoine","dayOfBirth","15",0,15).forEach(System.out::println); //streetAddress, title, manager
//        querier.searchPerson("Antoine","monthOfBirth","1",0,15).forEach(System.out::println); //streetAddress, title, manager
//        querier.searchPerson("Antoine","yearOfBirth","198",0,15).forEach(System.out::println); //streetAddress, title, manager
        querier.logout();


    }

    private static void testPublicQuerier() {
        IPublicADQuerier adQuerier = new PublicADQuerier("dummy.query", "@Azertyuiop06200");
//        System.out.println("recherche de Antoines");
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
