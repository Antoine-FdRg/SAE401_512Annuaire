package fr.seinksansdooze.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Contiens toutes les infos publiques sur une structure.
 */
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullStructure extends PartialStructure {
    private List<PartialStructure> subStructures;
    private List<PartialPerson> members;

    public FullStructure(String result, List<PartialStructure> subStructures, List<PartialPerson> members) {
        super(result);
        this.subStructures = subStructures;
        this.members = members;
    }

    @Override
    public String toString() {
        return "FullStructure{" +
                "dn=" + this.getDn() +
                ", subStructures=" + subStructures +
                ", members=" + members +
                "} ";
    }
}
