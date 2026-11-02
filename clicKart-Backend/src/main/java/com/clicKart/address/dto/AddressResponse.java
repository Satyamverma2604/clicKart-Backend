package com.clicKart.address.dto;



import com.clicKart.common.AddressType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressResponse {

    private Long id;

    private String houseNo;

    private String street;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private AddressType addressType;
}