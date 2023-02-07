package fr.seinksansdooze.backend.model;

/**
 * Contiens toutes les infos publiques sur une structure.
 */
public class Structure extends PartialStructure {
    private Structure[] children;

    public Structure(String ou, Structure[] children) {
        super(ou);
        this.children = children;
    }

    public Structure[] getChildren() {
        return children;
    }

    public void setChildren(Structure[] children) {
        this.children = children;
    }
}
