package fr.seinksansdooze.backend.model.payload;

public class UserAndGroupPayload {
    private final String dn;
    private final String groupCN;

    public UserAndGroupPayload(String dn, String groupCN) {
        this.dn = dn;
        this.groupCN = groupCN;
    }

    public String getDn() {
        return dn;
    }

    public String getGroupCN() {
        return groupCN;
    }
}
