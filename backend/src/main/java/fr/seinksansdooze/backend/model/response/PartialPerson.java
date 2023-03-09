package fr.seinksansdooze.backend.model.response;

/**
 * Un objet contenant des informations partielles sur une personne, renvoyé
 * dans les résultats de recherches d'une personne ou quand ce n'est pas pertinent
 * de récupérer la totalité des informations sur une personne.
 */
public class PartialPerson {
    private String firstName;
    private String lastName;
    private String cn;
    private String structureOU;

    public PartialPerson() {
    }

    public PartialPerson(String firstName, String lastName, String cn, String structureOU) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cn = cn;
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

    @Override
    public String toString() {
        return "PartialPerson{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cn='" + cn + '\'' +
                ", structureOU='" + structureOU + '\'' +
                '}';
    }
}
