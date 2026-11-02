package com.clicKart.user.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clicKart.common.Role;
import com.clicKart.exception.BadRequestException;
import com.clicKart.exception.DuplicateResourceException;
import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.user.dto.UserRequest;
import com.clicKart.user.dto.UserResponse;
import com.clicKart.user.entity.User;
import com.clicKart.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponse createUser(UserRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new DuplicateResourceException("Email already exists");
		}

		if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
			throw new DuplicateResourceException("Phone number already exists");
		}
		
		String encodedPassword =
		        passwordEncoder.encode(request.getPassword());

		User user = new User();
		
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(encodedPassword);
		user.setPhone(request.getPhone());

		User savedUser = userRepository.save(user);

		return mapToResponse(savedUser);
	}

	@Override
	public UserResponse getUserById(Long id) {

		User user = getUserByIdOrThrow(id);
		return mapToResponse(user);
	}

	@Override
	public List<UserResponse> getAllUsers() {

		return userRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	@Override
	public UserResponse updateUser(Long id, UserRequest request) {

		User user = getUserByIdOrThrow(id);

		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setPhone(request.getPhone());

		User updatedUser = userRepository.save(user);

		return mapToResponse(updatedUser);
	}

	@Override
	public void deleteUser(Long id) {

		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found with id"+id);
		}

		userRepository.deleteById(id);
	}
	
	@Override
	public UserResponse updateUserStatus(Long id, Boolean enabled) {

	    User user = getUserByIdOrThrow(id);

	    if (enabled == null) {
	        throw new BadRequestException(
	                "Enabled status is required");
	    }

	    user.setEnabled(enabled);

	    User updatedUser = userRepository.save(user);

	    return mapToResponse(updatedUser);
	}

	
	@Override
	public UserResponse updateUserRole(Long id, String role) {

	    User user = getUserByIdOrThrow(id);

	    if (role == null || role.isBlank()) {
	        throw new BadRequestException(
	                "Role is required");
	    }

	    try {
	        Role userRole = Role.valueOf(role.toUpperCase());

	        user.setRole(userRole);

	    } catch (IllegalArgumentException ex) {

	        throw new BadRequestException(
	                "Invalid role. Allowed roles: USER, ADMIN");
	    }

	    User updatedUser = userRepository.save(user);

	    return mapToResponse(updatedUser);
	}
	
	
	/*******************************************
	 * HELPER METHODS *
	 *******************************************/
	
	
	private User getUserByIdOrThrow(Long id) {
		// TODO Check Category exists or not
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id "+id));

	}

	private UserResponse mapToResponse(User user) {

		UserResponse response = new UserResponse();

		response.setId(user.getId());
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setPhone(user.getPhone());
		response.setRole(user.getRole());
		response.setEnabled(user.getEnabled());
		response.setCreatedAt(user.getCreatedAt());
		response.setUpdatedAt(user.getUpdatedAt());

		return response;
	}
}