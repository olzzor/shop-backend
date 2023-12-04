package com.bridgeshop.module.product.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDto {
    private ProductDto product;
    private String filePath;
    private String fileName;
    private int displayOrder;
}
