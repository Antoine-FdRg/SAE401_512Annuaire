package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.PartialPerson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
