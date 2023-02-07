package fr.seinksansdooze.backend.model;

/**
 * Un objet contenant des informations partielles sur une personne, renvoyé
 * dans les résultats de recherches d'une personne.
 */
public class PartialPerson {
    private String firstName;
    private String lastName;
    private String login;
    private String cn;
    private String email;
    private String structureOU;

    public PartialPerson() {
    }

    public PartialPerson(String firstName, String lastName, String login, String cn, String email, String structureOU) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.cn = cn;
        this.email = email;
        this.structureOU = structureOU;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStructureOU() {
        return structureOU;
    }

    public void setStructureOU(String structureOU) {
        this.structureOU = structureOU;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
