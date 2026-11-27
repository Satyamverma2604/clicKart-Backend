package com.clicKart.address.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clicKart.address.entity.Address;
import com.clicKart.common.AddressType;



@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

   
  
	  List<Address> findByUserIdAndDeletedFalse(Long userId);

	    // Get an active address by ID
	    Optional<Address> findByIdAndDeletedFalse(Long id);

	    // Check duplicate active addresses
	    boolean existsByUserIdAndAddressTypeAndHouseNoAndStreetAndCityAndStateAndCountryAndPostalCodeAndDeletedFalse(
	            Long userId,
	            AddressType addressType,
	            String houseNo,
	            String street,
	            String city,
	            String state,
	            String country,
	            String postalCode
	    );
}