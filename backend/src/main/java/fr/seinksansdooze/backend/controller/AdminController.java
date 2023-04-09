
package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiterSingleton;
import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.payload.UserAndGroupPayload;
import fr.seinksansdooze.backend.model.response.FullPerson;
import fr.seinksansdooze.backend.model.response.FullStructure;
import fr.seinksansdooze.backend.model.response.PartialGroup;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.naming.NamingException;
import java.util.List;

/**
 * Controller permettant de gérer les requêtes des administrateurs connectés
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    ADConnectionManager connectionManager = ADConnectionManagerSingleton.INSTANCE.get();

    @Operation(summary = "Renvoie toutes les informations disponibles sur une personne")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie"),
            @ApiResponse(responseCode = "401", description = "Token invalide")
    })
    @GetMapping("/info/person")
    public FullPerson personInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String dn, ServerHttpRequest request) {
        RateLimiterSingleton.get().tryConsume(String.valueOf(request.getLocalAddress()));

        try {
            return connectionManager.getQuerier(token).getFullPersonInfo(dn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }


    @Operation(summary = "Renvoie la liste des sous-structures ou personnes contenues dans une structure")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie"),
            @ApiResponse(responseCode = "401", description = "Token invalide"),
            @ApiResponse(responseCode = "404", description = "Structure non trouvée")
    })
    @GetMapping("/info/structure/{dn}")
    public FullStructure getStructureInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String dn, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));

        try {
            return connectionManager.getQuerier(token).getStructureInfo(dn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @Operation(summary = "Renvoie la liste de tout les groupes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie"),
            @ApiResponse(responseCode = "401", description = "Token invalide")
    })
    @GetMapping("/group/all")
    public List<PartialGroup> getAllGroups(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));

        try {
            return connectionManager.getQuerier(token).getAllGroups();
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @Operation(summary = "Créer un groupe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Groupe créé"),
            @ApiResponse(responseCode = "401", description = "Token invalide"),
            @ApiResponse(responseCode = "409", description = "Erreur lors de la création du groupe")
    })
    @PostMapping("/group/create")
    public ResponseEntity<String> createGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody PartialGroup group, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));

        System.out.println("name = " + group.getCn());
        boolean isGroupCreated;
        try {
            isGroupCreated = connectionManager.getQuerier(token).createGroup(group.getCn());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return isGroupCreated ? new ResponseEntity<>("Groupe créé avec succès.", HttpStatus.OK) : new ResponseEntity<>("Erreur lors de la création du groupe.", HttpStatus.CONFLICT);
    }

    @Operation(summary = "Renvoie la liste des membres d'un groupe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie"),
            @ApiResponse(responseCode = "401", description = "Token invalide")
    })
    @GetMapping("/group/members/{cn}")
    public List<PartialPerson> getGroupMembers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String cn, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        //TODO throw 404 if group not found et pas 401
        try {
            return connectionManager.getQuerier(token).getGroupMembers(cn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @Operation(summary = "Supprime un groupe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Groupe supprimé"),
            @ApiResponse(responseCode = "401", description = "Token invalide")
    })
    @DeleteMapping("/group/delete")
    public ResponseEntity<String> deleteGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody PartialGroup group, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));

        try {
            connectionManager.getQuerier(token).deleteGroup(group.getCn());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }

        return new ResponseEntity<>("Groupe supprimé avec succès.", HttpStatus.OK);
    }

    @Operation(summary = "Ajoute un utilisateur à un groupe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur ajouté au groupe"),
            @ApiResponse(responseCode = "401", description = "Token invalide"),
            @ApiResponse(responseCode = "406", description = "Erreur lors de l'ajout de l'utilisateur au groupe")
    })
    @PutMapping("/group/addUser")
    public ResponseEntity<String> addUserToGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody UserAndGroupPayload payload, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));

        boolean isUserAdded;
        try {
            isUserAdded = connectionManager.getQuerier(token).addUserToGroup(payload.getDn(), payload.getGroupCN());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return isUserAdded ? new ResponseEntity<>("Utilisateur ajouté au groupe avec succès.", HttpStatus.OK) : new ResponseEntity<>("Erreur lors de l'ajout de l'utilisateur au groupe.", HttpStatus.NOT_ACCEPTABLE);
    }

    @Operation(summary = "Supprime un utilisateur d'un groupe")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur supprimé du groupe"),
            @ApiResponse(responseCode = "401", description = "Token invalide"),
            @ApiResponse(responseCode = "406", description = "Erreur lors de la suppression de l'utilisateur du groupe")
    })
    @DeleteMapping("/group/removeUser")
    public ResponseEntity<String> removeUserFromGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody UserAndGroupPayload payload, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));


        boolean isUserRemoved;
        try {
            isUserRemoved = connectionManager.getQuerier(token).removeUserFromGroup(payload.getDn(), payload.getGroupCN());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return isUserRemoved ? new ResponseEntity<>("Utilisateur supprimé du groupe avec succès.", HttpStatus.OK) : new ResponseEntity<>("Erreur lors de la suppression de l'utilisateur du groupe.", HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/search/person")
    public List<PartialPerson> searchPersonAsAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String name, @RequestParam String filter, @RequestParam String value,
                                                   @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int perPage, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return connectionManager.getQuerier(token).searchPerson(name, filter, value, page, perPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/allFilters")
    public List<String> getAllFilters(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return connectionManager.getQuerier(token).getAllFilters();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
