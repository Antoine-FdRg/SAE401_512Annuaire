package fr.seinksansdooze.backend.model.response;

import lombok.SneakyThrows;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

public class LoggedInUser extends PartialPerson{
    private int isAdmin;

    public LoggedInUser(String firstName, String lastName, String cn, String structureOU, String position, int isAdmin) {
        super(firstName, lastName, cn, structureOU, position);
        this.isAdmin = isAdmin;
    }
    @SneakyThrows(NamingException.class)
    public LoggedInUser(SearchResult result) {
        super(result);
        this.isAdmin = Integer.parseInt(result.getAttributes().get("adminCount").get().toString());
    }

    public int isAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
                "firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", cn='" + getCn() + '\'' +
                ", structureOU='" + getStructureOU() + '\'' +
                ", position='" + getPosition() + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
