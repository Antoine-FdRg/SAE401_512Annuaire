package fr.seinksansdooze.backend.connectionManaging.ADBridge;

import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;

import java.util.ArrayList;

public interface IPublicADQuerier {

    ArrayList<PartialPerson> searchPerson(String cn);

    ArrayList<PartialStructure> searchStructure(String ou);

    PartialPerson getPartialPersonInfo(String cn);

    PartialStructure getPartialStructureInfo(String ou);
}
