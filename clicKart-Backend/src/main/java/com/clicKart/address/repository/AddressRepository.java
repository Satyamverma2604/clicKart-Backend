package com.clicKart.address.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clicKart.address.entity.Address;
import com.clicKart.common.AddressType;



@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);//Get all addresses of a user
  

    void deleteByUserId(Long userId);//  Delete all addresses of a user
    
    boolean existsByUserIdAndAddressTypeAndHouseNoAndStreetAndCityAndStateAndCountryAndPostalCode(
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