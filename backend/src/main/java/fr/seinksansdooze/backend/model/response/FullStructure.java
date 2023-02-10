package fr.seinksansdooze.backend.model.response;

import java.util.List;

/**
 * Contiens toutes les infos publiques sur une structure.
 */
public class FullStructure extends PartialStructure {
    private List<FullStructure> children;

    public FullStructure(String ou, List<FullStructure> children) {
        super(ou);
        this.children = children;
    }

    public FullStructure() {
    }

    public List<FullStructure> getChildren() {
        return children;
    }

    public void setChildren(List<FullStructure> children) {
        this.children = children;
    }
}
