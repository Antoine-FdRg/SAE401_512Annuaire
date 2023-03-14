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
public class SeinkSansDoozeErrorResponse {
    private String error;
}
