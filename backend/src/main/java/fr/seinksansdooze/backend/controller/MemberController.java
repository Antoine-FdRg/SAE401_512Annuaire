package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManagerSingleton;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiterSingleton;
import fr.seinksansdooze.backend.model.exception.SeinkSansDoozeBackException;
import fr.seinksansdooze.backend.model.payload.ModifAttribPayload;
import fr.seinksansdooze.backend.model.response.FullPerson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.naming.NamingException;

/**
 * Controller permettant de gérer les requêtes des membres connectés
 */
@RestController
@RequestMapping("/api/member")
public class MemberController {
    ADConnectionManager connectionManager = ADConnectionManagerSingleton.INSTANCE.get();

//    @PostMapping("/changePassword")
//    public ResponseEntity changePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody String cn, @RequestBody String oldPassword, @RequestBody String newPassword, ServerHttpRequest request){
//        RateLimiterSingleton.get().tryConsume(String.valueOf(request.getLocalAddress()));
//
//
//        try {
//            connectionManager.getQuerier(token).changePassword(cn, oldPassword, newPassword);
//        } catch (NamingException e) {
//            throw new SeinkSansDoozeBackException(
//                    HttpStatus.NOT_ACCEPTABLE,
//                    "Token introuvable");
    /**
     * Permet de modifier un attribut d'un utilisateur
     * @param token le token de l'utilisateur
     * @param payload le payload contenant l'attribut et la valeur à modifier
     * @param request la requête
     * @return 200 OK ou une erreur
     */
    @Operation(summary = "Permet de modifier un attribut d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attribut modifié avec succés."),
            @ApiResponse(responseCode = "400", description = "L'attribut ou la valeur n'est pas valide."),
            @ApiResponse(responseCode = "401", description = "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."),
            @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas.")
    })
    @PutMapping("/modify")
    public ResponseEntity<String> modifyAttribute(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody ModifAttribPayload payload, ServerHttpRequest request){
        RateLimiterSingleton.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            connectionManager.getQuerier(token).modifyAttribute(payload.getAttribute(), payload.getValue());
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Permet d'obtenir les informations personnelles d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informations personnelles récupérées avec succés."),
            @ApiResponse(responseCode = "400", description = "Erreur de formulation de la requête."),
            @ApiResponse(responseCode = "401", description = "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."),
            @ApiResponse(responseCode = "404", description = "L'utilisateur n'existe pas.")
    })
    @GetMapping("/getInfo")
    public FullPerson getInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, ServerHttpRequest request){
        RateLimiterSingleton.get().tryConsume(String.valueOf(request.getLocalAddress()));
        try {
            return connectionManager.getQuerier(token).getInfo();
        } catch (NamingException e) {
            throw new SeinkSansDoozeBackException(
                    HttpStatus.UNAUTHORIZED,
                    "Erreur de connexion, la session a peut être expirée, veuillez vous reconnecter."
            );
        }
    }

 }
