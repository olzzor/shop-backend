package com.shop.module.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.module.cart.dto.CartDto;
import com.shop.module.category.dto.CategoryDto;
import com.shop.module.contact.dto.ContactDto;
import com.shop.module.contact.entity.Contact;
import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.order.dto.OrderDetailDto;
import com.shop.module.product.entity.ProductStatus;
import com.shop.module.wishlist.dto.WishlistDto;
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
    private int price;
    private int discountPer;
    @JsonProperty("isDisplay")
    private boolean isDisplay;
    private ProductStatus status;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    private ProductDetailDto detail;
    private List<OrderDetailDto> orderDetails;
    private List<ProductSizeDto> productSizes;
    private List<ProductImageDto> productImages;
    private List<CartDto> carts;
    private List<WishlistDto> wishlists;
    private List<CouponDto> coupons;
    private List<ContactDto> contacts; // 20240521 추가

    // Builder pattern constructor
    @Builder
    public ProductDto(Long id, String code, String name, int price, int discountPer, boolean isDisplay,
                      ProductStatus status, LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.price = price;
        this.discountPer = discountPer;
        this.isDisplay = isDisplay;
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

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }

    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
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

    public void setDetail(ProductDetailDto productDetailDto) {
        this.detail = productDetailDto;
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

    public void setWishlists(List<WishlistDto> wishlistDtoList) {
        this.wishlists = wishlistDtoList;
    }

    public void setCoupons(List<CouponDto> couponDtoList) {
        this.coupons = couponDtoList;
    }

    public void setContacts(List<ContactDto> contactDtoList) {
        this.contacts = contactDtoList;
    }
}
