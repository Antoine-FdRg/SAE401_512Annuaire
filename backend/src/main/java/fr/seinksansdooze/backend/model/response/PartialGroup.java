package fr.seinksansdooze.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialGroup {
    private String cn;

    public PartialGroup(SearchResult sr) {
        try {
            this.cn = sr.getAttributes().get("cn").get().toString();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
