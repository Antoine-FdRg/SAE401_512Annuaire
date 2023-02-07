package backback;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

public class TestADQuerier {

    public static void main(String[] args) {
        ADQuerier adQuerier = new ADQuerier();
        System.out.println("api/search/person   api/search/structures   api/admin/group/all");
        NamingEnumeration<SearchResult> results = adQuerier.search(ObjectType.GROUP, "test");
        displayResults(results, ObjectType.PERSON);

        System.out.println("api/info/person/{cn}");
        results = adQuerier.searchPerson("Thomas TG. Gorisse");
        displayResults(results, ObjectType.PERSON);

        System.out.println("api/info/structure/{ou}");
        results = adQuerier.searchStructure("OU=Direction Général Adj,OU=512Direction,OU=512Batiment,OU=512BankFR,DC=EQUIPE1B,DC=local");
        displayResults(results, ObjectType.STRUCTURE);

        System.out.println("api/admin/group/all");
        results = adQuerier.searchAllGroups();
        displayResults(results, ObjectType.GROUP);

        System.out.println("api/admin/group/create/{groupName}");
        if (adQuerier.createGroup("test")) {
            System.out.println("Group created");
        } else {
            System.out.println("Group not created");
        }
//
//        System.out.println("api/admin/group/delete/{groupName}");
//        if(adQuerier.deleteGroup("test")){
//            System.out.println("Group deleted");
//        } else {
//            System.out.println("Group not deleted");
//        }

        System.out.println("api/admin/group/add/{groupName}/{cn}");
        if (adQuerier.addUserToGroup("test", "Thomas TG. Gorisse")) {
            System.out.println("User added to group");
        } else {
            System.out.println("User not added to group");
        }

//        System.out.println("api/admin/group/remove/{groupName}/{username}");
//        if(adQuerier.removeUserFromGroup("test", "Thomas TG. Gorisse")){
//            System.out.println("User removed from group");
//        } else {
//            System.out.println("User not removed from group");
//        }

        System.out.println("api/admin/search/person/{person}/{groupName}");
        results = adQuerier.searchPerson("Thomas TG. Gorisse", "test");
        displayResults(results, ObjectType.GROUP);

    }

    private static void displayResults(NamingEnumeration<SearchResult> results, ObjectType searchedObject) {
        System.out.println("Résultats :");
        SearchResult searchResult;
        while (true) {
            try {
                if (!results.hasMore()) break;
                searchResult = results.next();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
            Attributes attributes = searchResult.getAttributes();
            System.out.println(attributes.get(searchedObject.getNamingAttribute()));
//            System.out.println(attributes.get("member"));
        }
        System.out.println("\n");
    }
}
