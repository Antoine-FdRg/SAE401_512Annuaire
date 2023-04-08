package fr.seinksansdooze.backend.model.response;

import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserIncomplete;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private String dateOfBirth;
    private String managerDN;

    public FullPerson(String firstName, String lastName, String cn, String dn, String title, String login, String email, String personalPhone, String professionalPhone, String dateOfBirth, String address, String managerFullName, String managerDN) {
        super(firstName, lastName, dn, title);
        this.login = login;
        this.email = email;
        this.personalPhone = personalPhone;
        this.professionalPhone = professionalPhone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.managerDN = managerDN;
    }

    public FullPerson(SearchResult result) {
        super(result);
        try {
            this.login = result.getAttributes().get("sAMAccountName").toString().split(": ")[1];
            this.email = result.getAttributes().get("mail").toString().split(": ")[1];
            this.personalPhone = result.getAttributes().get("mobile").toString().split(": ")[1];
            this.professionalPhone = result.getAttributes().get("telephoneNumber").toString().split(": ")[1];
            this.address = result.getAttributes().get("streetAddress").toString().split(": ")[1];
            String dateOfBirth = result.getAttributes().get("dateDeNaissance").toString().split(": ")[1];
            this.dateOfBirth = dateOfBirth.substring(6, 8) + "/" + dateOfBirth.substring(4, 6) + "/" + dateOfBirth.substring(0, 4);
            this.managerDN = result.getAttributes().get("manager").toString().split(": ")[1];
        } catch (NullPointerException e) {
            throw new SeinkSansDoozeUserIncomplete();
        }
    }
}
