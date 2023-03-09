package fr.seinksansdooze.backend.model.response;

/**
 * Un objet qui contient toutes les informations qu'un utilisateur public peut
 * obtenir sur une personne.
 */
public class FullPerson extends PartialPerson {
    private String login;
    private String email;
    private String personalPhone;
    private String professionalPhone;
    private String address;
    private String managerFullName;
    private String managerCN;

    public FullPerson() {
    }

    public FullPerson(String firstName, String lastName, String cn, String structureOU, String login, String email, String personalPhone, String professionalPhone, String address, String managerFullName, String managerCN) {
        super(firstName, lastName, cn, structureOU);
        this.login = login;
        this.email = email;
        this.personalPhone = personalPhone;
        this.professionalPhone = professionalPhone;
        this.address = address;
        this.managerFullName = managerFullName;
        this.managerCN = managerCN;
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
