package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

public interface IPublicADQuerier {

    NamingEnumeration<SearchResult> search(ObjectType searchType, String searchValue);

    NamingEnumeration<SearchResult> searchPerson(String cn);

    NamingEnumeration<SearchResult> searchStructure(String ou);
}
