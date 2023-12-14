package com.bridgeshop.module.address.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public AddressUpdateRequest(String name, String phoneNumber, String zipCode, String province,
                                String city, String address1, String address2,
                                Boolean isApartment, Boolean isDefault) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.province = province;
        this.city = city;
        this.address1 = address1;
        this.address2 = address2;
        this.isApartment = (isApartment == null) ? false : isApartment; // 기본값 설정
        this.isDefault = (isDefault == null) ? false : isDefault; // 기본값 설정
        // 관계형 필드는 생성자에서 초기화하지 않음
    }
}
