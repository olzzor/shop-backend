package com.bridgeshop.module.address.dto;

import com.bridgeshop.module.user.dto.UserDto;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public AddressDto(Long id, UserDto user, String name, String phoneNumber,
                      String zipCode, String province, String city,
                      String address1, String address2, Boolean isApartment, Boolean isDefault) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.province = province;
        this.city = city;
        this.address1 = address1;
        this.address2 = address2;
        this.isApartment = isApartment;
        this.isDefault = isDefault;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setIsApartment(boolean isApartment) {
        this.isApartment = isApartment;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
