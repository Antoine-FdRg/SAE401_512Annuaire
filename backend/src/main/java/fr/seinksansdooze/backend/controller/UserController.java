package fr.seinksansdooze.backend.controller;

import fr.seinksansdooze.backend.model.payload.LoginPayload;
import fr.seinksansdooze.backend.model.response.LoginResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginPayload payload) {



        ResponseCookie responseCookie = ResponseCookie.from("token","TODO")
                .httpOnly(true)
                //.secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
//                .domain("")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();


    }

    @PostMapping("/logout")
    public void logout() {

    }
}
