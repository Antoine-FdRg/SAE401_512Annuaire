package fr.seinksansdooze.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.directory.SearchResult;

/**
 * Un objet contenant des informations partielles sur une personne, renvoyé
 * dans les résultats de recherches d'une personne ou quand ce n'est pas pertinent
 * de récupérer la totalité des informations sur une personne.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialPerson {
    private String firstName;
    private String lastName;
    private String cn;
    private String structureOU;
    private String position;

    /**
     * Constructeur à partir d'un résultat de recherche LDAP
     *
     * @param person le résultat de recherche LDAP
     */
    public PartialPerson(SearchResult person) {
        try {
            this.firstName = person.getAttributes().get("givenName").get().toString();
            this.lastName = person.getAttributes().get("sn").get().toString();
            this.cn = person.getAttributes().get("cn").get().toString();
            this.structureOU = person.getAttributes().get("distinguishedName").toString().split(": ")[1];
            this.position = this.buildPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildPosition() {
        String[] ou = this.structureOU.split(",");
        for (int i = 0; i < ou.length; i++) {
            ou[i] = ou[i].split("=")[1];
        }
        String position;
        if(ou[1].equals("Direction")){
            position = ou[1] + " du " + ou[2];
        }else {
            position = ou[1];
        }
        return position;
    }
}
