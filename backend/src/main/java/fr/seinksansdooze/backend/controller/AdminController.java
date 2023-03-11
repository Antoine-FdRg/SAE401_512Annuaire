
package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.connectionManaging.ADBridge.IAdminADQuerier;
import fr.seinksansdooze.backend.connectionManaging.ADConnectionManager;
import fr.seinksansdooze.backend.connectionManaging.TokenGenerator;
import fr.seinksansdooze.backend.connectionManaging.TokenSanitizer;
import fr.seinksansdooze.backend.model.payload.ChangeGroupsPayload;
import fr.seinksansdooze.backend.model.payload.LoginPayload;
import fr.seinksansdooze.backend.model.response.LoginResponse;
import fr.seinksansdooze.backend.model.response.PartialGroup;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    ADConnectionManager connectionManager;

    AdminController() {
        TokenGenerator tg = new TokenGenerator();
        TokenSanitizer ts = new TokenSanitizer();
        this.connectionManager = new ADConnectionManager(tg,ts,IAdminADQuerier.class);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginPayload payload) {
        try {
            return new LoginResponse(this.connectionManager.addConnection(payload));
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

//    @GetMapping("/ping/{token}")
//    public String ping(@PathVariable String token) {
//        try {
//            if (connectionManager.getQuerier(token).getClass()!= null) {
//                return "ping réussi";
//            }
//            throw new RuntimeException("Token invalide");
//        } catch (NamingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @GetMapping("/group/all")
    public List<PartialGroup> getAllGroups(@RequestBody LoginResponse loginResponse) {
        try {
            return connectionManager.getQuerier(loginResponse.getToken()).getAllGroups();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/logout")
    public void logout() {
        // Faire la déconnexion ici...
    }

    @GetMapping("/info/person/{cn}")
    public PartialPerson personInfo(@PathVariable String cn) {
//       return connectionManager.getQuerier("Test token").getFullPersonInfo(cn);
        try{
            return connectionManager.getQuerier("Test token").getFullPersonInfo(cn);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    @PutMapping("/add/{cn}")
    public void addAdmin(@PathVariable String cn) {
        // Ajouter un utilisateur comme admin ici...
    }

    @DeleteMapping("/delete/{cn}")
    public void removeAdmin(@PathVariable String cn) {
        // Enlever un utilisateur comme admin ici...
    }
}
