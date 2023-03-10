package fr.seinksansdooze.backend.model.response;

import javax.naming.directory.SearchResult;

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

    /**
     * Constructeur d'une personne à partir de ses informations
     * @param firstName le prénom
     * @param lastName le nom
     * @param cn le cn
     * @param structureOU l'unité d'organisation / la structure
     */
    public PartialPerson(String firstName, String lastName, String cn, String structureOU) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cn = cn;
        this.structureOU = structureOU;
    }
    /**
     * Constructeur à partir d'un résultat de recherche LDAP
     * @param person le résultat de recherche LDAP
     */
    public PartialPerson(SearchResult person) {
        this.firstName = person.getAttributes().get("givenName").toString();
        this.lastName = person.getAttributes().get("sn").toString();
        this.cn = person.getAttributes().get("cn").toString();
        this.structureOU = person.getAttributes().get("distinguishedName").toString().split(",")[1].split("=")[1];
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
