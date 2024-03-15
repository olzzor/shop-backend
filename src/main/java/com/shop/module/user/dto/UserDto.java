package com.shop.module.user.dto;

import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.favorite.dto.FavoriteDto;
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
    private boolean adminFlag;
    private boolean activateFlag;
    private List<FavoriteDto> favorites;
    private List<CouponDto> coupons;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public UserDto(Long id, String name, String email, String password, String phoneNumber,
                   AuthProvider authProvider, String socialId, boolean adminFlag, boolean activateFlag,
                   LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.authProvider = authProvider;
        this.socialId = socialId;
        this.adminFlag = adminFlag;
        this.activateFlag = activateFlag;
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

    public void setAdminFlag(boolean adminFlag) {
        this.adminFlag = adminFlag;
    }

    public void setActivateFlag(boolean activateFlag) {
        this.activateFlag = activateFlag;
    }

    public void setFavorites(List<FavoriteDto> favoriteDtoList) {
        this.favorites = favoriteDtoList;
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
