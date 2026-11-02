package com.clicKart.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clicKart.security.principal.UserPrincipal;
import com.clicKart.user.dto.UserRequest;
import com.clicKart.user.dto.UserResponse;
import com.clicKart.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create User - Public
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get Logged-in User
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {

        UserResponse response =
                userService.getUserById(principal.getId());

        return ResponseEntity.ok(response);
    }

    // Update Logged-in User
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UserRequest request) {

        UserResponse response =
                userService.updateUser(principal.getId(), request);

        return ResponseEntity.ok(response);
    }

    // Delete Logged-in User
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(
            @AuthenticationPrincipal UserPrincipal principal) {

        userService.deleteUser(principal.getId());

        return ResponseEntity.noContent().build();
    }
}