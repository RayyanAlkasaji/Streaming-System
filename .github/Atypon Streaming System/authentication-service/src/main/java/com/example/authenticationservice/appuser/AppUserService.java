package com.example.authenticationservice.appuser;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class AppUserService {

    private final AppUserRepo appUserRepo;
    public String getNameByEmail(String email){
        AppUser user = appUserRepo.findAppUserByUsername(email);
        return user.getName();
    }
    public Long getIdByEmail(String email){
        AppUser user = appUserRepo.findAppUserByUsername(email);
        return user.getUserId();
    }

    public AppUser getAppUser(String username){
        return appUserRepo.findAppUserByUsername(username);
    }

    public List<AppUser> getUsers() {
        return appUserRepo.findAll();
    }

    public AppUser createUser(AppUser appUser) {
        AppUser userByUsername = appUserRepo.findAppUserByUsername(appUser.getUsername());
        if(userByUsername != null){
            throw new IllegalStateException("email is taken");
        }
        return appUserRepo.save(appUser);

    }

    public String login(LoggedUser user, HttpServletRequest request){
        AppUser userByUsername = appUserRepo.findAppUserByUsername(user.getUsername());
        if(userByUsername == null){
            throw new IllegalStateException("user not found");
        }
        if (!userByUsername.getPassword().equals(user.getPassword())){
            throw new IllegalStateException("wrong credentials");
        }
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

    }

}
