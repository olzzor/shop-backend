package com.bridgeshop.module.address.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateRequest {
    private Long id;
    private String name;
    private String phoneNumber;
    private String zipCode;
    private String province;
    private String city;
    private String address1;
    private String address2;
    @JsonProperty("isApartment")
    private boolean isApartment;
    @JsonProperty("isDefault")
    private boolean isDefault;
}
