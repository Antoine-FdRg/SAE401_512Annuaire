package fr.seinksansdooze.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 * Un objet qui contient toutes les informations qu'un utilisateur public peut
 * obtenir sur une personne.
 */
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullPerson extends PartialPerson {
    private String login;
    private String email;
    private String personalPhone;
    private String professionalPhone;
    private String address;
    private String managerFullName;
    private String managerCN;

    public FullPerson(String firstName, String lastName, String cn, String structureOU, String position, String login, String email, String personalPhone, String professionalPhone, String address, String managerFullName, String managerCN) {
        super(firstName, lastName, cn, structureOU, position);
        this.login = login;
        this.email = email;
        this.personalPhone = personalPhone;
        this.professionalPhone = professionalPhone;
        this.address = address;
        this.managerFullName = managerFullName;
        this.managerCN = managerCN;
    }

    public FullPerson(SearchResult result) {
        super(result);
        //TODO: ajouter les attributs manquants dans l'AD
        this.login = result.getAttributes().get("sAMAccountName").toString().split(": ")[1];
        this.email = result.getAttributes().get("mail").toString().split(": ")[1];
        this.personalPhone = result.getAttributes().get("mobile").toString().split(": ")[1];
        this.professionalPhone = result.getAttributes().get("telephoneNumber").toString().split(": ")[1];
        this.address = result.getAttributes().get("streetAddress").toString().split(": ")[1];
        this.managerFullName = result.getAttributes().get("manager").toString().split(",")[0].split("=")[1];
        this.managerCN = result.getAttributes().get("manager").toString().split(": ")[1];
    }
}
