package com.clicKart.auth.service;

import com.clicKart.auth.dto.LoginRequest;
import com.clicKart.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}