package fr.seinksansdooze.backend.connectionManaging.ADBridge.model;

import java.util.ArrayList;
import java.util.List;

public enum ADFilter {
    TITLE("title"),
    ADDRESS("address"),
    MANAGER("managerDN"),
    DAYOFBIRTH("dayOfBirth"),
    MONTHOFBIRTH("monthOfBirth"),
    YEAROFBIRTH("yearOfBirth");


    private final String filter;

    ADFilter(String filter){
        this.filter = filter;
    }

    public static List<String> getAllFilters() {
        List<String> filters = new ArrayList<>();
        for (ADFilter filter : ADFilter.values()) {
            filters.add(filter.getFilter());
        }
        return filters;
    }

    public String getFilter(){
        return this.filter;
    }
}
