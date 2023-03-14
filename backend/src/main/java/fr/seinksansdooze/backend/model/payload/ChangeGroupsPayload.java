package fr.seinksansdooze.backend.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Liste des groupes à assigner à une personne.
 * Utilisée pour la route <code>PUT /api/admin/group/person/&lt;cn&gt;</code>.
 */
@Data
@AllArgsConstructor
public class ChangeGroupsPayload {
    private List<String> groups;
}
