package com.clicKart.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.clicKart.common.Role;
import com.clicKart.user.entity.User;
import com.clicKart.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@clicKart.com";

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = new User();

        admin.setName("clicKart Admin");
        admin.setEmail(adminEmail);
        admin.setPassword(
                passwordEncoder.encode("Admin@123")
        );
        admin.setPhone("9999999999");
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);

        userRepository.save(admin);

        System.out.println("=================================");
        System.out.println("Default admin created successfully");
        System.out.println("Email    : " + adminEmail);
        System.out.println("Password : Admin@123");
        System.out.println("=================================");
    }
}