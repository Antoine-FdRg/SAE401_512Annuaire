
package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.ChangeGroupsPayload;
import fr.seinksansdooze.backend.model.LoginPayload;
import fr.seinksansdooze.backend.model.LoginResponse;
import fr.seinksansdooze.backend.model.PartialPerson;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginPayload payload) {
        return new LoginResponse("Test token");
    }

    @PostMapping("/logout")
    public void logout() {
        // Faire la déconnexion ici...
    }

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
}
