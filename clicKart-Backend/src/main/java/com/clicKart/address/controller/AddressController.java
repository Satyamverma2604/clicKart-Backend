package com.clicKart.address.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clicKart.address.dto.AddressRequest;
import com.clicKart.address.dto.AddressResponse;
import com.clicKart.address.service.AddressService;
import com.clicKart.security.principal.UserPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/me/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response =
                addressService.createAddress(
                        principal.getId(),
                        request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getUserAddresses(
            @AuthenticationPrincipal UserPrincipal principal) {

        List<AddressResponse> response =
                addressService.getUserAddresses(
                        principal.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long addressId) {

        AddressResponse response =
                addressService.getAddressById(
                        principal.getId(),
                        addressId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request) {

        AddressResponse response =
                addressService.updateAddress(
                        principal.getId(),
                        addressId,
                        request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long addressId) {

        addressService.deleteAddress(
                principal.getId(),
                addressId);

        return ResponseEntity.noContent().build();
    }
}