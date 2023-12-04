package com.bridgeshop.module.favorite.dto;

import com.bridgeshop.module.user.dto.UserDto;
import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.product.dto.ProductSizeDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDto {
    private Long id;
    private UserDto user;
    private ProductDto product;
    private ProductSizeDto productSize;
}
