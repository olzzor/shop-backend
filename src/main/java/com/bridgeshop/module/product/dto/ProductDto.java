package com.bridgeshop.module.product.dto;

import com.bridgeshop.module.cart.dto.CartDto;
import com.bridgeshop.module.category.dto.CategoryDto;
import com.bridgeshop.module.coupon.dto.CouponDto;
import com.bridgeshop.module.favorite.dto.FavoriteDto;
import com.bridgeshop.module.order.dto.OrderDetailDto;
import com.bridgeshop.module.product.entity.ProductStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String code;
    private CategoryDto category;
    private String name;
    private String detail;
    private int price;
    private int discountPer;
    private ProductStatus status;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private List<OrderDetailDto> orderDetails;
    private List<ProductSizeDto> productSizes;
    private List<ProductImageDto> productImages;
    private List<CartDto> carts;
    private List<FavoriteDto> favorites;
    private List<CouponDto> coupons;
}
