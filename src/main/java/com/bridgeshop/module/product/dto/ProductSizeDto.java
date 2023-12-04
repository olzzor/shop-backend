package com.bridgeshop.module.product.dto;

import com.bridgeshop.module.cart.dto.CartProductDto;
import com.bridgeshop.module.favorite.dto.FavoriteDto;
import com.bridgeshop.module.order.dto.OrderDetailDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSizeDto {
    private Long id;
    private ProductDto product;
    private String size;
    private int quantity;
    private List<FavoriteDto> favorites;
    private List<CartProductDto> cartProducts;
    private List<OrderDetailDto> orderDetails; // 추가 근데 필요함?
}
