
package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import fr.seinksansdooze.backend.model.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.payload.ChangeGroupsPayload;
import fr.seinksansdooze.backend.model.response.FullPerson;
import fr.seinksansdooze.backend.model.response.PartialGroup;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import java.util.List;

/**
 * Controller permettant de gérer les requêtes des administrateurs connectés
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    ADConnectionManager connectionManager = ADConnectionManagerSingleton.INSTANCE.get();

//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Ping réussi"),
//            @ApiResponse(responseCode = "401", description = "Token invalide")
//    })
//    @GetMapping("/ping")
//    public String ping(@CookieValue("token") String token) {
//        if (RateLimiterSingleton.INSTANCE.get().tryConsume("request.getRemoteAddr()")){
//            try {
//                if (connectionManager.getQuerier(token).getClass()!= null) {
//                    return "ping réussi";
//                }
//                throw new RuntimeException("Token invalide");
//            } catch (NamingException e) {
//                throw new SeinkSansDoozeBackException(
//                        HttpStatus.UNAUTHORIZED,
//                        "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
//                );
//            }
//        }
//        throw new SeinkSansDoozeBackException(
//                HttpStatus.TOO_MANY_REQUESTS,
//                "Trop de requêtes, veuillez patienter."
//        );
//    }


    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie"),
            @ApiResponse(responseCode = "401", description = "Token invalide")
    })
    @GetMapping("/info/person")
    public FullPerson personInfo(@CookieValue("token") String token, @RequestParam String cn) {
        try {
            //TODO @ba101397 bloquer les perm de lectures des attributs que l'on considère comme sensibles pour les utilisateurs non admin
            return connectionManager.getQuerier(token).getFullPersonInfo(cn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    // TODO : implémenter la verification correctement
    @GetMapping("/group/all")
    public List<PartialGroup> getAllGroups(@CookieValue("token") String token) {
        try {
            return connectionManager.getQuerier(token).getAllGroups();
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }
    

//    @GetMapping("/info/person/{cn}")
//    public PartialPerson personInfo(@PathVariable String cn) {
//        // TODO: 3/12/2023 vrai verification (voir todo d'au dessus)
////       return connectionManager.getQuerier("Test token").getFullPersonInfo(cn);
//        try{
//            return connectionManager.getQuerier("Test token").getFullPersonInfo(cn);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    // TODO: 2/10/2023 GET /api/admin/group/all

    // TODO: 2/10/2023 POST /api/admin/group/create

    @DeleteMapping("/group/delete/{name}")
    public void deleteGroup(@PathVariable String name) {
        // Faire la suppression de groupe ici...
    }

    @PutMapping("/group/person/{cn}")
    public void changeGroups(@RequestBody ChangeGroupsPayload payload, @PathVariable String cn) {
        // Faire la modification de groupes ici...
    }

    @GetMapping("/search/person")
    public List<PartialPerson> searchPersonAsAdmin(@RequestParam String name, @RequestParam String group) {
        return List.of();
    }

    @PutMapping("/add/{cn}")
    public void addAdmin(@PathVariable String cn) {
        // Ajouter un utilisateur comme admin ici...
    }

    @DeleteMapping("/delete/{cn}")
    public void removeAdmin(@PathVariable String cn) {
        // Enlever un utilisateur comme admin ici...
    }
}
