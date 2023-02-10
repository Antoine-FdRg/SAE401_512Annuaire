package fr.seinksansdooze.backend.model.response;

/**
 * Une structure quand elle apparait dans les résultats de recherches.
 */
public class PartialStructure {
    private String ou;

    public PartialStructure(String ou) {
        this.ou = ou;
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
