package fr.seinksansdooze.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
}
