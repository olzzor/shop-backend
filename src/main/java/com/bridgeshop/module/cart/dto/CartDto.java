package com.bridgeshop.module.cart.dto;

import com.bridgeshop.module.product.dto.ProductDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;
    private boolean activateFlag;
    private List<ProductDto> products;
}
