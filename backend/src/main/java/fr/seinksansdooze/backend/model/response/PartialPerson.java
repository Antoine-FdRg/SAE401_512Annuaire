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
    private String position;

    public PartialPerson() {
    }

    /**
     * Constructeur d'une personne à partir de ses informations
     *
     * @param firstName   le prénom
     * @param lastName    le nom
     * @param cn          le cn
     * @param structureOU l'unité d'organisation / la structure
     */
    public PartialPerson(String firstName, String lastName, String cn, String structureOU) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cn = cn;
        this.structureOU = structureOU;
        this.position = this.buildPosition();
    }

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
        String position = "";
        if(ou[1].equals("Direction")){
            position = ou[1] + " du " + ou[2];
        }else {
            position = ou[1];
        }
        return position;
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
                ", position='" + position + '\'' +
                '}';
    }
}
