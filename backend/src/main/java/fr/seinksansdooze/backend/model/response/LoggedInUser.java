package fr.seinksansdooze.backend.model.response;

import lombok.SneakyThrows;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchResult;
import java.util.Objects;

public class LoggedInUser extends PartialPerson{
    private boolean isAdmin;

    @SneakyThrows(NamingException.class)
    public LoggedInUser(SearchResult result) {
        super(result);
        Attribute admin = result.getAttributes().get("adminCount");
        if(admin == null){
            this.isAdmin = false;
        }else{
            this.isAdmin = Objects.equals(admin.get().toString(), "1");
        }
    }

    public LoggedInUser() {
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
