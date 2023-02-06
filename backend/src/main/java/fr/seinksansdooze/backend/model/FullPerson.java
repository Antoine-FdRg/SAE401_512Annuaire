package fr.seinksansdooze.backend.model;

/**
 * Un objet qui contient toutes les informations qu'un utilisateur public peut
 * obtenir sur une personne.
 */
public class FullPerson extends PartialPerson {
    private String personalPhone;
    private String professionalPhone;
    private String address;
    private String managerFullName;
    private String managerLogin;

    public FullPerson() {
    }

    public FullPerson(String firstName, String lastName, String login, String email, String role, String structure, String personalPhone, String professionalPhone, String address, String managerFullName, String managerLogin) {
        super(firstName, lastName, login, email, role, structure);
        this.personalPhone = personalPhone;
        this.professionalPhone = professionalPhone;
        this.address = address;
        this.managerFullName = managerFullName;
        this.managerLogin = managerLogin;
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

    public String getManagerLogin() {
        return managerLogin;
    }

    public void setManagerLogin(String managerLogin) {
        this.managerLogin = managerLogin;
    }
}
