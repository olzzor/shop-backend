package com.bridgeshop.module.product.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductImageDto {
    private ProductDto product;
    private String filePath;
    private String fileName;
    private int displayOrder;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ProductImageDto(String filePath, String fileName, int displayOrder) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.displayOrder = displayOrder;
    }

    // 설정자 메서드들
    public void setProduct(ProductDto productDto) {
        this.product = productDto;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
