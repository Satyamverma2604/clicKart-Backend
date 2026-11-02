
package com.clicKart.address.dto;

import com.clicKart.common.AddressType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressRequest {

    @NotBlank(message = "House number is required")
    @Size(max = 50, message = "House number must not exceed 50 characters")
    private String houseNo;

    @NotBlank(message = "Street is required")
    @Size(max = 150, message = "Street must not exceed 150 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @NotBlank(message = "Postal code is required")
    @Pattern(
        regexp = "^[1-9][0-9]{5}$",
        message = "Postal code must be a valid 6-digit Indian PIN code"
    )
    private String postalCode;

    @NotNull(message = "Address type is required")
    private AddressType addressType;
}

