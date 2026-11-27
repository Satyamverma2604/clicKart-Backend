
package com.clicKart.address.service;

import java.util.List;

import com.clicKart.address.dto.AddressRequest;
import com.clicKart.address.dto.AddressResponse;

public interface AddressService {

    AddressResponse createAddress(Long userId, AddressRequest request);

    List<AddressResponse> getUserAddresses(Long userId);

    AddressResponse getAddressById(Long userId, Long addressId);

    AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request);

    void deleteAddress(Long userId, Long addressId);
}
