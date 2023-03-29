package fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces;

import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;

import java.util.List;

public interface IPublicADQuerier {

    List<PartialPerson> searchPerson(String cn, int page, int perPage);

    List<PartialStructure> searchStructure(String ou, int page, int perPage);

    PartialPerson getPartialPersonInfo(String cn);

    PartialStructure getPartialStructureInfo(String ou);

    boolean userExists(String username);
}
