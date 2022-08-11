package com.example.thymeleaf.repositories;

import com.example.thymeleaf.entity.App_Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<App_Users, Long> {

    App_Users findByUsername(String email);
}
