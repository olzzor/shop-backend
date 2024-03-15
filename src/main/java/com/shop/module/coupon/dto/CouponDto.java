package com.shop.module.coupon.dto;

import com.shop.module.category.dto.CategoryDto;
import com.shop.module.coupon.entity.CouponDiscountType;
import com.shop.module.coupon.entity.CouponStatus;
import com.shop.module.coupon.entity.CouponType;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponDto(Long id, CouponType type, String code, String name, String detail,
                     int minAmount, CouponDiscountType discountType, int discountValue,
                     LocalDateTime startValidDate, LocalDateTime endValidDate, CouponStatus status) {
        this.id = id;
        this.type = type;
        this.code = code;
        this.name = name;
        this.detail = detail;
        this.minAmount = minAmount;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startValidDate = startValidDate;
        this.endValidDate = endValidDate;
        this.status = status;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public void setDiscountType(CouponDiscountType discountType) {
        this.discountType = discountType;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public void setStartValidDate(LocalDateTime startValidDate) {
        this.startValidDate = startValidDate;
    }

    public void setEndValidDate(LocalDateTime endValidDate) {
        this.endValidDate = endValidDate;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}

