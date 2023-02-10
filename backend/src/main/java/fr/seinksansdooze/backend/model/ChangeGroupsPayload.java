package fr.seinksansdooze.backend.model;

/**
 * Liste des groupes à assigner à une personne.
 * Utilisée pour la route <code>PUT /api/admin/group/person/&lt;cn&gt;</code>.
 */
public class ChangeGroupsPayload {
    private String[] groups;

    public ChangeGroupsPayload() {
    }

    public String[] getGroups() {
        return groups;
    }

    public void setGroups(String[] groups) {
        this.groups = groups;
    }
}
