package com.example.authenticationservice.appuser;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody LoggedUser user , HttpServletRequest request){

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/login").toUriString());
        return ResponseEntity.created(uri).body(appUserService.login(user,request));

    }
    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers(){
        return ResponseEntity.ok().body(appUserService.getUsers());
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/register").toUriString());

        return ResponseEntity.created(uri).body(appUserService.createUser(appUser));
    }

    @GetMapping("/userName/{email}")
    ResponseEntity<String>getNameByEmail(@PathVariable String email){
        return ResponseEntity.ok().body(appUserService.getNameByEmail(email));
    }

    @GetMapping("/id/{email}")
    ResponseEntity<Long>getIdByEmail(@PathVariable String email){
        return ResponseEntity.ok().body(appUserService.getIdByEmail(email));
    }


    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyUser(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String access_token = authorizationHeader.substring("Bearer ".length());
            DecodedJWT jwt = JWT.decode(access_token);
            String email = jwt.getSubject();
            AppUser appUserByUsername = appUserService.getAppUser(email);

            if (appUserByUsername == null || jwt.getExpiresAt().before(new Date())){
                return ResponseEntity.ok().body(false);
            }
            else{
                return ResponseEntity.ok().body(true);
            }
        }
        else {
            throw new RuntimeException("Access token is missing");
        }
    }

}
