package fr.seinksansdooze.backend.model.response;

import fr.seinksansdooze.backend.model.exception.user.SeinkSansDoozeUserIncomplete;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.naming.directory.SearchResult;
import java.text.Normalizer;
import java.time.LocalDate;

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
    private LocalDate dateOfBirth;
    private String managerDN;

    public FullPerson(String firstName, String lastName, String cn, String dn, String title, String login, String email, String personalPhone, String professionalPhone, String dateOfBirth, String address, String managerFullName, String managerDN) {
        super(firstName, lastName, dn, title);
        this.login = login;
        this.email = email;
        this.personalPhone = personalPhone;
        this.professionalPhone = professionalPhone;
        this.address = address;
        this.dateOfBirth = LocalDate.parse(dateOfBirth.substring(0, 4) + "-" + dateOfBirth.substring(4, 6) + "-" + dateOfBirth.substring(6, 8));
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
            this.dateOfBirth = LocalDate.parse(dateOfBirth.substring(0, 4) + "-" + dateOfBirth.substring(4, 6) + "-" + dateOfBirth.substring(6, 8));
            this.managerDN = result.getAttributes().get("manager").toString().split(": ")[1];
        } catch (NullPointerException e) {
            throw new SeinkSansDoozeUserIncomplete();
        }
    }

    public PartialPerson toPartialPerson() {
        return new PartialPerson(this.getFirstName(), this.getLastName(), this.getDn(), this.getTitle());
    }

    public boolean check(String filter, String value) {
        return switch (filter) {
            case "title" -> this.normalize(this.getTitle().toLowerCase()).contains(value.toLowerCase());
            case "address" -> this.normalize(this.getAddress().toLowerCase()).contains(value.toLowerCase());
            case "managerDN" -> this.normalize(this.getManagerDN().toLowerCase()).contains(value.toLowerCase());
            default -> false;
        };
    }

    private String normalize(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
