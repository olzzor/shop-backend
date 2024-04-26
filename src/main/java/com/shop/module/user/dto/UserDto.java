package com.shop.module.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.wishlist.dto.WishlistDto;
import com.shop.module.user.entity.AuthProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private AuthProvider authProvider;
    private String socialId;
    @JsonProperty("isAdmin")
    private boolean isAdmin;
    @JsonProperty("isActivate")
    private boolean isActivate;
    private List<WishlistDto> wishlists;
    private List<CouponDto> coupons;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public UserDto(Long id, String name, String email, String password, String phoneNumber,
                   AuthProvider authProvider, String socialId, boolean isAdmin, boolean isActivate,
                   LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.authProvider = authProvider;
        this.socialId = socialId;
        this.isAdmin = isAdmin;
        this.isActivate = isActivate;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    // 설정자 메서드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setActivate(boolean isActivate) {
        this.isActivate = isActivate;
    }

    public void setWishlists(List<WishlistDto> wishlistDtoList) {
        this.wishlists = wishlistDtoList;
    }

    public void setCoupons(List<CouponDto> couponDtoList) {
        this.coupons = couponDtoList;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setModDate(LocalDateTime modDate) {
        this.modDate = modDate;
    }
}
