package com.shop.module.coupon.mapper;

import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.coupon.entity.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CouponMapper {

    public CouponDto mapToDto(Coupon coupon) {
        return CouponDto.builder()
                .id(coupon.getId())
                .type(coupon.getType())
                .code(coupon.getCode())
                .name(coupon.getName())
                .detail(coupon.getDetail())
                .minAmount(coupon.getMinAmount())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .startValidDate(coupon.getStartValidDate())
                .endValidDate(coupon.getEndValidDate())
                .status(coupon.getStatus())
                .build();
    }

    public List<CouponDto> mapToDtoList(List<Coupon> couponList) {
        return couponList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}