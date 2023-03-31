package fr.seinksansdooze.backend.connectionManaging.ADBridge.model;

public enum ADFilter {
    CN("cn"),
    INITIALS("initials"),
    LASTNAME("sn"),
    POSTALCODE("postalCode");

    private final String filter;

    ADFilter(String filter){
        this.filter = filter;
    }

    public String getFilter(){
        return this.filter;
    }
}
