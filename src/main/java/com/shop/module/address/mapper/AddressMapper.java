package com.shop.module.address.mapper;

import com.shop.module.address.entity.Address;
import com.shop.module.address.dto.AddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AddressMapper {

    public AddressDto mapToDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .name(address.getName())
                .phoneNumber(address.getPhoneNumber())
                .zipCode(address.getZipCode())
                .province(address.getProvince())
                .city(address.getCity())
                .address1(address.getAddress1())
                .address2(address.getAddress2())
                .isApartment(address.isApartment())
                .isDefault(address.isDefault())
                .build();
    }

    public List<AddressDto> mapToDtoList(List<Address> addressList) {
        return addressList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}