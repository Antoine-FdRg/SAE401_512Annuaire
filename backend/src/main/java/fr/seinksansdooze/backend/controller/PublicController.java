package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.FullPerson;
import fr.seinksansdooze.backend.model.PartialPerson;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    @GetMapping("/search/person")
    public List<PartialPerson> searchPerson(@RequestParam String name) {
        return List.of(new PartialPerson(
                "Thomas",
                "Gorisse",
                "tgorisse",
                "thomas.gorisse@ssd.bank",
                "Directeur général",
                "TODO ???"
                ),
                new PartialPerson(
                        "Thomas",
                        "Gorisse",
                        "tgorisse",
                        "thomas.gorisse@ssd.bank",
                        "Directeur général",
                        "TODO ???"
                )
        );
    }

    @GetMapping("/search/structure")
    public List<Object> searchStructure(@RequestParam String name) {
        throw new UnsupportedOperationException("Cette route n'a pas été implémentée");
    }

    @GetMapping("/info/person/{login}")
    public FullPerson personInfo(@PathVariable String login) {
        return new FullPerson();
    }

    // TODO: 06/02/2023 completer
    @GetMapping("/info/structure/{}")
    public Object structureInfo() {
        throw new UnsupportedOperationException("Cette route n'a pas été implémentée");
    }
}
