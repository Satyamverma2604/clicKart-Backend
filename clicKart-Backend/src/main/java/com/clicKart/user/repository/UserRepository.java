package com.clicKart.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clicKart.user.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);//for jwt auth

    boolean existsByPhone(String phone);//for jwt auth
}
