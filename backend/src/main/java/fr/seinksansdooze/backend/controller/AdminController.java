
package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiterSingleton;
import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.payload.NewPersonPayload;
import fr.seinksansdooze.backend.model.payload.UserAndGroupPayload;
import fr.seinksansdooze.backend.model.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "Renvoie toutes les informations disponibles sur une personne à partir de son DN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie"),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "404", description = "Personne non trouvée.")
    })
    @GetMapping("/info/person")
    public FullPerson personInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String dn, ServerHttpRequest request) {
        RateLimiterSingleton.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).getFullPersonInfo(dn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @Operation(summary = "Renvoie la liste des sous-structures ou personnes contenues dans une structure.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie, la liste des sous-structures et des personnes est retournée."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "404", description = "Structure non trouvée.")
    })
    @GetMapping("/info/structure/{dn}")
    public FullStructure getStructureInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String dn, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).getStructureInfo(dn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @Operation(summary = "Crée un utilisateur.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succèss"),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "409", description = "Erreur lors de la création de l'utilisateur.")
    })
    @PostMapping("/member/create")
    public ResponseEntity<SeinkSansDoozeSuccessResponse> createUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Valid @RequestBody NewPersonPayload payload, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).createPerson(payload);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return new ResponseEntity<>(new SeinkSansDoozeSuccessResponse("Utilisateur créé avec succès."), HttpStatus.CREATED);
    }

    @Operation(summary = "Supprime un utilisateur de l'Active Directory.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur supprimé."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé.")
    })
    @DeleteMapping("/member/delete")
    public ResponseEntity<SeinkSansDoozeSuccessResponse> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String dn, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).deletePerson(dn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return new ResponseEntity<>(new SeinkSansDoozeSuccessResponse("Utilisateur supprimé avec succès."), HttpStatus.OK);
    }


    @Operation(summary = "Renvoie la liste de tout les groupes de l'Active Directory.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie"),
            @ApiResponse(responseCode = "401", description = "Token invalide")
    })
    @GetMapping("/group/all")
    public List<PartialGroup> getAllGroups(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).getAllGroups();
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @Operation(summary = "Crée un groupe dans l'Active Directory.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Groupe créé avec succèss."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "409", description = "Le groupe existe déjà.")
    })
    @PostMapping("/group/create")
    public ResponseEntity<SeinkSansDoozeSuccessResponse> createGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody PartialGroup group, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        boolean isGroupCreated;
        try {
            isGroupCreated = ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).createGroup(group.getCn());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        if (!isGroupCreated) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.CONFLICT,
                    "Le groupe existe déjà."
            );
        }
        return new ResponseEntity<>(new SeinkSansDoozeSuccessResponse("Groupe créé avec succès "), HttpStatus.CREATED);
    }

    @Operation(summary = "Renvoie la liste des membres d'un groupe à partir de son CN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "404", description = "Groupe non trouvé.")
    })
    @GetMapping("/group/members/{cn}")
    public List<PartialPerson> getGroupMembers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable String cn, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).getGroupMembers(cn);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @Operation(summary = "Supprime un groupe de l'Active Directory à partir de son CN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Groupe supprimé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "404", description = "Groupe non trouvé.")
    })
    @DeleteMapping("/group/delete")
    public ResponseEntity<SeinkSansDoozeSuccessResponse> deleteGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody PartialGroup group, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).deleteGroup(group.getCn());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }

        return new ResponseEntity<>(new SeinkSansDoozeSuccessResponse("Groupe supprimé avec succès."), HttpStatus.OK);
    }

    @Operation(summary = "Ajoute un utilisateur à un groupe à partir de son DN et du CN du groupe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur ajouté au groupe avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou groupe non trouvé."),
            @ApiResponse(responseCode = "409", description = "L'utilisateur est déjà dans le groupe.")
    })
    @PutMapping("/group/addUser")
    public ResponseEntity<SeinkSansDoozeSuccessResponse> addUserToGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody UserAndGroupPayload payload, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        boolean isUserAdded;
        try {
            isUserAdded = ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).addUserToGroup(payload.getDn(), payload.getGroupCN());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        if (!isUserAdded) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.CONFLICT,
                    "L'utilisateur est déjà dans le groupe."
            );
        }
        return new ResponseEntity<>(new SeinkSansDoozeSuccessResponse("Utilisateur ajouté au groupe avec succès."), HttpStatus.OK);
    }

    @Operation(summary = "Supprime un utilisateur d'un groupe.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur supprimé du groupe."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou groupe non trouvé."),
    })
    @DeleteMapping("/group/removeUser")
    public ResponseEntity<SeinkSansDoozeSuccessResponse> removeUserFromGroup(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody UserAndGroupPayload payload, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        boolean isUserRemoved;
        try {
            isUserRemoved = ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).removeUserFromGroup(payload.getDn(), payload.getGroupCN());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        if (!isUserRemoved) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.NOT_FOUND,
                    "L'utilisateur ne semble pas être dans le groupe."
            );
        }
        return new ResponseEntity<>(new SeinkSansDoozeSuccessResponse("Utilisateur supprimé du groupe avec succès."), HttpStatus.OK);
    }

    @Operation(summary = "Recherche un utilisateur dans l'Active Directory selon un filtre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "401", description = "Token invalide."),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé.")
    })
    @GetMapping("/search/person")
    public List<PartialPerson> searchPersonAsAdmin(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String name, @RequestParam String filter, @RequestParam String value,
                                                   @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int perPage, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).searchPerson(name, filter, value, page, perPage);
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

    @Operation(summary = "Recherche un groupe dans l'Active Directory selon un filtre.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recherche réussie."),
            @ApiResponse(responseCode = "401", description = "Token invalide.")
    })
    @GetMapping("/allFilters")
    public List<String> getAllFilters(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, ServerHttpRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return ADConnectionManagerSingleton.INSTANCE.get().getQuerier(token).getAllFilters();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
