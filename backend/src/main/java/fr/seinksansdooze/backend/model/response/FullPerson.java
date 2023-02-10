package fr.seinksansdooze.backend.model.response;

/**
 * Un objet qui contient toutes les informations qu'un utilisateur public peut
 * obtenir sur une personne.
 */
public class FullPerson extends PartialPerson {
    private String personalPhone;
    private String professionalPhone;
    private String address;
    private String managerFullName;
    private String managerCN;

    public FullPerson() {
    }

    public FullPerson(String firstName, String lastName, String login, String cn, String email, String structureOU, String personalPhone, String professionalPhone, String address, String managerFullName, String managerCN) {
        super(firstName, lastName, login, cn, email, structureOU);
        this.personalPhone = personalPhone;
        this.professionalPhone = professionalPhone;
        this.address = address;
        this.managerFullName = managerFullName;
        this.managerCN = managerCN;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    public String getProfessionalPhone() {
        return professionalPhone;
    }

    public void setProfessionalPhone(String professionalPhone) {
        this.professionalPhone = professionalPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getManagerFullName() {
        return managerFullName;
    }

    public void setManagerFullName(String managerFullName) {
        this.managerFullName = managerFullName;
    }

    public String getManagerCN() {
        return managerCN;
    }

    public void setManagerCN(String managerCN) {
        this.managerCN = managerCN;
    }
}
