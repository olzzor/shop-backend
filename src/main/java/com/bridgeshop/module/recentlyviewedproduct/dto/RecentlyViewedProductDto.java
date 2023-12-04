package com.bridgeshop.module.recentlyviewedproduct.dto;

import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.user.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentlyViewedProductDto {
    private Long id;
    private UserDto user;
    private ProductDto product;
    private LocalDateTime viewedAt;
}
