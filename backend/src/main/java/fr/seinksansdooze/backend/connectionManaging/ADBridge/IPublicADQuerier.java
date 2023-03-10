package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.model.response.FullPerson;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;

public interface IPublicADQuerier {

    ArrayList<PartialPerson> searchPerson(String cn);

    ArrayList<PartialStructure> searchStructure(String ou);

    PartialPerson getPersonInfo(String cn);

    PartialStructure getStructureInfo(String ou);
}
