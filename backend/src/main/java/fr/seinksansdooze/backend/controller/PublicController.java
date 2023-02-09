package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.FullPerson;
import fr.seinksansdooze.backend.model.PartialPerson;
import fr.seinksansdooze.backend.model.PartialStructure;
import fr.seinksansdooze.backend.model.FullStructure;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    @GetMapping("/search/person")
    public List<PartialPerson> searchPerson(@RequestParam String name) {
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
