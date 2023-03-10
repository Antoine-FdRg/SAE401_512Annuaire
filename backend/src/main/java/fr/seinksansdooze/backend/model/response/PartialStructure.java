package fr.seinksansdooze.backend.model.response;

import javax.naming.directory.SearchResult;

/**
 * Une structure quand elle apparait dans les résultats de recherches.
 */
public class PartialStructure {
    private String ou;

    public PartialStructure(String ou) {
        this.ou = ou;
    }

    public PartialStructure(SearchResult sr) {
        this.ou = sr.getNameInNamespace().split(",")[0].split("=")[1];
    }

    public PartialStructure() {
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }
}
