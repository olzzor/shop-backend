package com.shop.module.product.dto;

import com.shop.module.cart.dto.CartProductDto;
import com.shop.module.wishlist.dto.WishlistDto;
import com.shop.module.order.dto.OrderDetailDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductSizeDto {
    private Long id;
    private ProductDto product;
    private String size;
    private int quantity;
    private List<WishlistDto> wishlists;
    private List<CartProductDto> cartProducts;
    private List<OrderDetailDto> orderDetails; // 추가 근데 필요함?

    // Builder pattern constructor
    @Builder
    public ProductSizeDto(Long id, String size, int quantity) {
        this.id = id;
        this.size = size;
        this.quantity = quantity;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(ProductDto productDto) {
        this.product = productDto;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setWishlists(List<WishlistDto> wishlistDtoList) {
        this.wishlists = wishlistDtoList;
    }

    public void setCartProducts(List<CartProductDto> cartProductDtoList) {
        this.cartProducts = cartProductDtoList;
    }

    public void setOrderDetails(List<OrderDetailDto> orderDetailDtoList) {
        this.orderDetails = orderDetailDtoList;
    }
}
