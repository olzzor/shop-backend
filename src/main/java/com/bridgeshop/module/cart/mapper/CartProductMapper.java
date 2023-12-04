package com.bridgeshop.module.cart.mapper;

import com.bridgeshop.module.cart.dto.CartProductDto;
import com.bridgeshop.module.cart.entity.CartProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartProductMapper {

    public CartProductDto mapToDto(CartProduct cartProduct) {
        return CartProductDto.builder()
                .id(cartProduct.getId())
                .quantity(cartProduct.getQuantity())
                .build();
    }

    public List<CartProductDto> mapToDtoList(List<CartProduct> cartProductList) {
        return cartProductList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}