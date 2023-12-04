package com.bridgeshop.module.user.dto;

import com.bridgeshop.module.coupon.dto.CouponDto;
import com.bridgeshop.module.user.entity.AuthProvider;
import com.bridgeshop.module.favorite.dto.FavoriteDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
