package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.model.response.PartialPerson;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;

public interface IPublicADQuerier {

    ArrayList<PartialPerson> searchPerson(String cn);

    NamingEnumeration<SearchResult> searchStructure(String ou);
}
