package fr.seinksansdooze.backend.connectionManaging.ADBridge.model;

import java.util.ArrayList;
import java.util.List;

public enum ADFilter {
    TITLE("Poste","title"),
    ADDRESS("Adresse","address"),
    MANAGER("Manager","managerDN"),
    DAYOFBIRTH("Jour de naissance","dayOfBirth"),
    MONTHOFBIRTH("Mois de naissance","monthOfBirth"),
    YEAROFBIRTH("Année de naissance","yearOfBirth");

    private final String filter;
    private final String display;

    ADFilter(String display, String filter){
        this.filter = filter;
        this.display = display;
    }

    public static List<String> getAllFilters() {
        List<String> filters = new ArrayList<>();
        for (ADFilter filter : ADFilter.values()) {
            filters.add(filter.getDisplay());
        }
        return filters;
    }

    public String getFilter(){
        return this.filter;
    }

    public String getDisplay(){
        return this.display;
    }

    public static String getFilter(String display){
        for (ADFilter f : ADFilter.values()) {
            if(f.getDisplay().equals(display)){
                return f.getFilter();
            }
        }
        return null;
    }

}
