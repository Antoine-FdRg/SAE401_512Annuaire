package fr.seinksansdooze.backend.model.payload;

public class AddUserToGroupPayload {
    private final String cn;
    private final String groupName;

    public AddUserToGroupPayload(String cn, String groupName) {
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
