package fr.seinksansdooze.backend.model;

import java.util.List;

/**
 * Liste des groupes à assigner à une personne.
 * Utilisée pour la route <code>PUT /api/admin/group/person/&lt;cn&gt;</code>.
 */
public class ChangeGroupsPayload {
    private List<String> groups;

    public ChangeGroupsPayload() {
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
