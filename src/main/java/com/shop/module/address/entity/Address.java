package com.shop.module.address.entity;

import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "addresses")
public class Address extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String zipCode;

    @Column(nullable = false, length = 50)
    private String province;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 100)
    private String address1;

    @Column(length = 100)
    private String address2;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isApartment;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDefault;


    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Address(User user, String name, String phoneNumber, String zipCode,
                   String province, String city, String address1, String address2,
                   Boolean isApartment, Boolean isDefault) {
        this.user = user;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.zipCode = zipCode;
        this.province = province;
        this.city = city;
        this.address1 = address1;
        this.address2 = address2;
        this.isApartment = (isApartment == null) ? false : isApartment; // 기본값 설정
        this.isDefault = (isDefault == null) ? false : isDefault; // 기본값 설정
    }

    // 설정자 메서드들
    public void setUser(User user) {
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