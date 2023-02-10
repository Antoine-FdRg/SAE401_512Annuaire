
package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.LoginPayload;
import fr.seinksansdooze.backend.model.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginPayload payload) {
        return new LoginResponse("Test token");
    }
}
