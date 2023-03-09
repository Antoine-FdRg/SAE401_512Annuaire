package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.response.FullPerson;
import fr.seinksansdooze.backend.model.response.FullStructure;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import fr.seinksansdooze.backend.model.response.PartialStructure;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Les résultats sont disponibles"),
            @ApiResponse(responseCode = "400", description = "Il manque un ou plusieurs paramètres")
    })
    @GetMapping("/search/person")
    public List<PartialPerson> searchPerson(@RequestParam String name, @RequestParam int page, @RequestParam int perPage) {
        return List.of();
    }

    @GetMapping("/search/structure")
    public List<PartialStructure> searchStructure(@RequestParam String name) {
        return List.of();
    }

    @GetMapping("/info/person/{cn}")
    public FullPerson personInfo(@PathVariable String cn) {
        return new FullPerson();
    }

    @GetMapping("/info/structure/{ou}")
    public FullStructure structureInfo(@PathVariable String ou) {
        return new FullStructure();
    }
}
