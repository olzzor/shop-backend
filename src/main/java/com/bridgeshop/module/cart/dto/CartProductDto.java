package com.bridgeshop.module.cart.dto;

import com.bridgeshop.module.coupon.dto.CouponDto;
import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.product.dto.ProductSizeDto;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDto {
    private Long id;
    private CartDto cart;
    private ProductDto product;
    private ProductSizeDto productSize;
    private int quantity;
    private CouponDto coupon;
}

