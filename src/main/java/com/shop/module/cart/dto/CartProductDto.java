package com.shop.module.cart.dto;

import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.wishlist.dto.WishlistInfo;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductSizeDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartProductDto {
    private Long id;
    private CartDto cart;
    private ProductDto product;
    private ProductSizeDto productSize;
    private int quantity;
    private CouponDto coupon;

    private WishlistInfo wishlistInfo; // 추가

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CartProductDto(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setCart(CartDto cart) {
        this.cart = cart;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public void setProductSize(ProductSizeDto productSize) {
        this.productSize = productSize;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCoupon(CouponDto coupon) {
        this.coupon = coupon;
    }

    public void setWishlistInfo(WishlistInfo wishlistInfo) {
        this.wishlistInfo = wishlistInfo;
    }
}

