package com.bridgeshop.module.coupon.dto;

import com.bridgeshop.module.coupon.entity.CouponDiscountType;
import com.bridgeshop.module.coupon.entity.CouponStatus;
import com.bridgeshop.module.coupon.entity.CouponType;
import com.bridgeshop.module.category.dto.CategoryDto;
import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.user.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponDto {
    private Long id;
    private CouponType type;
    private String code;
    private String name;
    private String detail;
    private int minAmount;
    private CouponDiscountType discountType;
    private int discountValue;
    private LocalDateTime startValidDate;
    private LocalDateTime endValidDate;
    private CouponStatus status;
    private List<CategoryDto> categories;
    private List<ProductDto> products;
    private List<UserDto> users;
}

