
package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.payload.ChangeGroupsPayload;
import fr.seinksansdooze.backend.model.response.PartialPerson;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
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

    // TODO : implémenter la verifification correctement
//    @GetMapping("/group/all")
//    public List<PartialGroup> getAllGroups(@RequestBody LoginResponse loginResponse) {
//        try {
//            return connectionManager.getQuerier(loginResponse.getToken()).getAllGroups();
//        } catch (NamingException e) {
//            throw new RuntimeException(e);
//        }
//    }
    

//    @GetMapping("/info/person/{cn}")
//    public PartialPerson personInfo(@PathVariable String cn) {
//        // TODO: 3/12/2023 vrai verification (voir todo d'au dessus)
////       return connectionManager.getQuerier("Test token").getFullPersonInfo(cn);
//        try{
//            return connectionManager.getQuerier("Test token").getFullPersonInfo(cn);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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
