package fr.seinksansdooze.backend.model.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifAttribPayload {
    String attribute;
    String value;

}
