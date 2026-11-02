
package com.clicKart.address.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clicKart.address.dto.AddressRequest;
import com.clicKart.address.dto.AddressResponse;
import com.clicKart.address.entity.Address;
import com.clicKart.address.repository.AddressRepository;
import com.clicKart.exception.BadRequestException;
import com.clicKart.exception.ResourceNotFoundException;
import com.clicKart.exception.ResourceOwnershipException;
import com.clicKart.user.entity.User;
import com.clicKart.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	@Override
	public AddressResponse createAddress(Long userId, AddressRequest request) {

		User user = getUserByIdOrThrow(userId);

		boolean duplicate = addressRepository
				.existsByUserIdAndAddressTypeAndHouseNoAndStreetAndCityAndStateAndCountryAndPostalCode(userId,
						request.getAddressType(), request.getHouseNo(), request.getStreet(), request.getCity(),
						request.getState(), request.getCountry(), request.getPostalCode());

		if (duplicate) {
			throw new BadRequestException("Duplicate address already exists");
		}

		Address address = new Address();

		address.setHouseNo(request.getHouseNo());
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setCountry(request.getCountry());
		address.setPostalCode(request.getPostalCode());
		address.setAddressType(request.getAddressType());

		address.setUser(user);

		Address savedAddress = addressRepository.save(address);

		return mapToResponse(savedAddress);
	}

	@Override
	public List<AddressResponse> getUserAddresses(Long userId) {
		getUserByIdOrThrow(userId);
		return addressRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
	}

	@Override
	public AddressResponse getAddressById(Long userId, Long addressId) {

		Address address = getAddressByIdOrThrow(addressId);

		verifyAddressByIdWithUserOrThrow(address, userId);

		return mapToResponse(address);
	}

	@Override
	public AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request) {

		Address address = getAddressByIdOrThrow(addressId);

		verifyAddressByIdWithUserOrThrow(address, userId);

		address.setHouseNo(request.getHouseNo());
		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setCountry(request.getCountry());
		address.setPostalCode(request.getPostalCode());
		address.setAddressType(request.getAddressType());

		Address updatedAddress = addressRepository.save(address);

		return mapToResponse(updatedAddress);
	}

	@Override
	public void deleteAddress(Long userId, Long addressId) {

		Address address = getAddressByIdOrThrow(addressId);

		verifyAddressByIdWithUserOrThrow(address, userId);

		addressRepository.delete(address);
	}

	/*******************************************
	 * HELPER METHODS *
	 *******************************************/

	private User getUserByIdOrThrow(Long id) {
		// TODO Check Category exists or not
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

	}

	private Address getAddressByIdOrThrow(Long id) {
		// TODO Check Category exists or not
		return addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + id));

	}

	private void verifyAddressByIdWithUserOrThrow(Address address, Long userId) {
		// TODO Check Address belongs with user or not
		if (!address.getUser().getId().equals(userId)) {
			throw new ResourceOwnershipException("Address does not belong to this user");
		}
	}

	private AddressResponse mapToResponse(Address address) {

		AddressResponse response = new AddressResponse();

		response.setId(address.getId());
		response.setHouseNo(address.getHouseNo());
		response.setStreet(address.getStreet());
		response.setCity(address.getCity());
		response.setState(address.getState());
		response.setCountry(address.getCountry());
		response.setPostalCode(address.getPostalCode());
		response.setAddressType(address.getAddressType());

		return response;
	}
}
