package fr.seinksansdooze.backend.model.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPersonPayload {
    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String structureDN;
    private String title;
    private String personalPhone;
    private String professionalPhone;
    private String dateOfBirth;
    private String address;
    private String managerDN;
}
