package com.clicKart.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clicKart.security.principal.UserPrincipal;
import com.clicKart.user.entity.User;
import com.clicKart.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user ;

		if (username.contains("@")) {

			user = userRepository.findByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

		} else {

			user = userRepository.findByPhone(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found with phone: " + username));
		}

		return new UserPrincipal(user);
	}
}
