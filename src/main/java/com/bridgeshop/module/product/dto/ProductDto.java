package com.bridgeshop.module.product.dto;

import com.bridgeshop.module.cart.dto.CartDto;
import com.bridgeshop.module.category.dto.CategoryDto;
import com.bridgeshop.module.coupon.dto.CouponDto;
import com.bridgeshop.module.favorite.dto.FavoriteDto;
import com.bridgeshop.module.order.dto.OrderDetailDto;
import com.bridgeshop.module.product.entity.ProductStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // Builder pattern constructor
    @Builder
    public ProductDto(Long id, String code, String name, String detail, int price, int discountPer,
                      ProductStatus status, LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.discountPer = discountPer;
        this.status = status;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCategory(CategoryDto categoryDto) {
        this.category = categoryDto;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setModDate(LocalDateTime modDate) {
        this.modDate = modDate;
    }

    public void setOrderDetails(List<OrderDetailDto> orderDetailDtoList) {
        this.orderDetails = orderDetailDtoList;
    }

    public void setProductSizes(List<ProductSizeDto> productSizeDtoList) {
        this.productSizes = productSizeDtoList;
    }

    public void setProductImages(List<ProductImageDto> productImageDtoList) {
        this.productImages = productImageDtoList;
    }

    public void setCarts(List<CartDto> cartDtoList) {
        this.carts = cartDtoList;
    }

    public void setFavorites(List<FavoriteDto> favoriteDtoList) {
        this.favorites = favoriteDtoList;
    }

    public void setCoupons(List<CouponDto> couponDtoList) {
        this.coupons = couponDtoList;
    }
}
