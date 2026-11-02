package com.clicKart.user.service;

import java.util.List;

import com.clicKart.user.dto.UserRequest;
import com.clicKart.user.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);
    
    UserResponse updateUserStatus(Long id, Boolean enabled);

    UserResponse updateUserRole(Long id, String role);
}