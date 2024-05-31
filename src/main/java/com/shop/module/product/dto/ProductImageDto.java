package com.shop.module.product.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductImageDto {
    private Long id;
    private ProductDto product;
    private String fileUrl;
    private String fileKey;
    private int displayOrder;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ProductImageDto(Long id, String fileUrl, String fileKey, int displayOrder) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.fileKey = fileKey;
        this.displayOrder = displayOrder;
    }

    // 설정자 메서드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(ProductDto productDto) {
        this.product = productDto;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
