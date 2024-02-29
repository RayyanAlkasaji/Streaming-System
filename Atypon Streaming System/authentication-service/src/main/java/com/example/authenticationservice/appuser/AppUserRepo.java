package com.example.authenticationservice.appuser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser,Long> {
    AppUser findAppUserByUsername(String username);


}
