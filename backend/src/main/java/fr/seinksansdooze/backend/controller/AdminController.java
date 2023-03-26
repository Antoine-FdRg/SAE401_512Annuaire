
package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import fr.seinksansdooze.backend.model.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.payload.UserAndGroupPayload;
import fr.seinksansdooze.backend.model.payload.ChangeGroupsPayload;
import fr.seinksansdooze.backend.model.response.FullPerson;
import fr.seinksansdooze.backend.model.response.PartialGroup;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/group/create")
    public ResponseEntity<String> createGroup(@CookieValue("token") String token, @RequestBody PartialGroup group) {
        System.out.println("name = " + group.getCn());
        boolean isGroupCreated;
        try {
            isGroupCreated =connectionManager.getQuerier(token).createGroup(group.getCn());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return isGroupCreated ? new ResponseEntity<>("Groupe créé avec succès.", HttpStatus.OK) : new ResponseEntity<>("Erreur lors de la création du groupe.", HttpStatus.CONFLICT);
    }


    @DeleteMapping("/group/delete/{cn}")
    public ResponseEntity<String> deleteGroup(@CookieValue("token") String token, @PathVariable String cn) {
        try {
            connectionManager.getQuerier(token).deleteGroup(cn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }

        return new ResponseEntity<>("Groupe supprimé avec succès.", HttpStatus.OK);
    }

    @GetMapping("/group/members/{cn}")
    public List<PartialPerson> getGroupMembers(@CookieValue("token") String token, @PathVariable String cn) {
        try {
            return connectionManager.getQuerier(token).getGroupMembers(cn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @PutMapping("/group/addUser")
    public ResponseEntity<String> addUserToGroup(@CookieValue("token") String token, @RequestBody UserAndGroupPayload payload) {
        boolean isUserAdded;
        try {
            isUserAdded = connectionManager.getQuerier(token).addUserToGroup(payload.getCn(), payload.getGroupName());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return isUserAdded ? new ResponseEntity<>("Utilisateur ajouté au groupe avec succès.", HttpStatus.OK) : new ResponseEntity<>("Erreur lors de l'ajout de l'utilisateur au groupe.", HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/group/removeUser")
    public ResponseEntity<String> removeUserFromGroup(@CookieValue("token") String token, @RequestBody UserAndGroupPayload payload) {
        boolean isUserRemoved;
        try {
            isUserRemoved = connectionManager.getQuerier(token).removeUserFromGroup(payload.getCn(), payload.getGroupName());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return isUserRemoved ? new ResponseEntity<>("Utilisateur supprimé du groupe avec succès.", HttpStatus.OK) : new ResponseEntity<>("Erreur lors de la suppression de l'utilisateur du groupe.", HttpStatus.NOT_ACCEPTABLE);
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
