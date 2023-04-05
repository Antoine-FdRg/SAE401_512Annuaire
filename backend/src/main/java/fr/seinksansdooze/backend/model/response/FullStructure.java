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
    private List<FullStructure> children;

    public FullStructure(String ou, List<FullStructure> children) {
        super(ou);
        this.children = children;
    }
}
