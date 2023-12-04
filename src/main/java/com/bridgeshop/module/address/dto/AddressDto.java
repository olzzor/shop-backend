package com.bridgeshop.module.address.dto;

import com.bridgeshop.module.user.dto.UserDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private UserDto user;
    private String name;
    private String phoneNumber;
    private String zipCode;
    private String province;
    private String city;
    private String address1;
    private String address2;
    private boolean isApartment;
    private boolean isDefault;
}
