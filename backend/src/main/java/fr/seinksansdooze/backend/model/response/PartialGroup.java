package fr.seinksansdooze.backend.model.response;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

public class PartialGroup {
    private String cn;

    public PartialGroup(String cn) {
        this.cn = cn;
    }

    public PartialGroup(SearchResult sr) {
        try {
            this.cn = sr.getAttributes().get("cn").get().toString();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    @Override
    public String toString() {
        return "PartialGroup{" +
                "cn='" + cn + '\'' +
                '}';
    }
}
