package fr.seinksansdooze.backend.model.payload;

public class UserAndGroupPayload {
    private final String cn;
    private final String groupName;

    public UserAndGroupPayload(String cn, String groupName) {
        this.cn = cn;
        this.groupName = groupName;
    }

    public String getCn() {
        return cn;
    }

    public String getGroupName() {
        return groupName;
    }
}
