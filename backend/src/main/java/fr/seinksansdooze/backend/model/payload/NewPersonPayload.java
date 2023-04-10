package fr.seinksansdooze.backend.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPersonPayload {
    private String firstName;
    private String lastName;
    private String structureDN;
    private String title;
    private String personalPhone;
    private String professionalPhone;
    private String dateOfBirth;
    private String address;
    private String managerDN;
}
