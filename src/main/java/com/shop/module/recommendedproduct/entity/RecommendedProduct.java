package com.shop.module.recommendedproduct.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "recommended_products")
public class RecommendedProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @Column(nullable = false, length = 10)
    private int displayOrder;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public RecommendedProduct(Product product, int displayOrder) {
        this.product = product;
        this.displayOrder = displayOrder;
    }

    // 설정자 메서드들
    public void setProduct(Product product) {
        this.product = product;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}

