package fr.seinksansdooze.backend.model;

/**
 * Contiens toutes les infos publiques sur une structure.
 */
public class FullStructure extends PartialStructure {
    private FullStructure[] children;

    public FullStructure(String ou, FullStructure[] children) {
        super(ou);
        this.children = children;
    }

    public FullStructure() {
    }

    public FullStructure[] getChildren() {
        return children;
    }

    public void setChildren(FullStructure[] children) {
        this.children = children;
    }
}
