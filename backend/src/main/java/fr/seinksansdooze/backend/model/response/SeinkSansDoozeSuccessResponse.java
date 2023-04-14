package fr.seinksansdooze.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Renvoyé quand une erreur se produit.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeinkSansDoozeSuccessResponse {
    private String msg = "Success";
}
