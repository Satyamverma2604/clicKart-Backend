package com.clicKart.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.clicKart.auth.dto.LoginRequest;
import com.clicKart.auth.dto.LoginResponse;
import com.clicKart.security.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;

	private final JwtUtil jwtUtil;

	@Value("${jwt.expiration}")
	private long expirationTime;

	@Override
	public LoginResponse login(LoginRequest request) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				request.getUsername(), request.getPassword());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		String username = authentication.getName();

		String token = jwtUtil.generateToken(username);

		return new LoginResponse(token, "Bearer", expirationTime);
	}
}