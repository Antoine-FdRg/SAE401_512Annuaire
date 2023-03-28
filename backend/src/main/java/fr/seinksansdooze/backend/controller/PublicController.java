package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.interfaces.IPublicADQuerier;
import fr.seinksansdooze.backend.connectionManaging.rateLimit.RateLimiterSingleton;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller permettant de gérer les requêtes publiques (faites par les utilisateurs non connectés)
 */
@RestController
@RequestMapping("/api/public")
@CrossOrigin
public class PublicController {

    private final IPublicADQuerier querier;

    public PublicController(IPublicADQuerier querier) {
        this.querier = querier;
    }

    @Operation(summary = "Recherche une personne en fonction de son nom")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Les résultats sont disponibles"),
            @ApiResponse(responseCode = "400", description = "Il manque un ou plusieurs paramètres")
    })
    @GetMapping("/search/person")
    public List<PartialPerson> searchPerson(@RequestParam String name,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "15") int perPage, HttpServletRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(request.getRemoteAddr());

        return querier.searchPerson(name, page, perPage);
    }

    @Operation(summary = "Recherche une structure en fonction de son nom")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Les résultats sont disponibles"),
            @ApiResponse(responseCode = "400", description = "Il manque un ou plusieurs paramètres")
    })
    @GetMapping("/search/structure")
    public List<PartialStructure> searchStructure(@RequestParam String name,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "25") int perPage, HttpServletRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(request.getRemoteAddr());

        return querier.searchStructure(name, page, perPage);
    }

    @Operation(summary = "Récupère les informations d'une personne en fonction de son cn")
    @GetMapping("/info/person/{cn}")
    public PartialPerson personInfo(@PathVariable String cn, HttpServletRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(request.getRemoteAddr());
        return querier.getPartialPersonInfo(cn);
    }

    @Operation(summary = "Récupère les informations d'une structure en fonction de son ou")
    @GetMapping("/info/structure/{ou}")
    public PartialStructure structureInfo(@PathVariable String ou, HttpServletRequest request) {
        RateLimiterSingleton.INSTANCE.get().tryConsume(request.getRemoteAddr());
        return querier.getPartialStructureInfo(ou);
    }
}
