package com.shop.module.cart.mapper;

import com.shop.module.cart.dto.CartDto;
import com.shop.module.cart.entity.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartMapper {

    public CartDto mapToDto(Cart cart) {
        return CartDto.builder()
                .id(cart.getId())
                .activateFlag(cart.isActivateFlag())
                .build();
    }

    public List<CartDto> mapToDtoList(List<Cart> cartList) {
        return cartList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}