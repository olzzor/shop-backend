package com.shop.module.product.entity;

import com.shop.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "product_details")
public class ProductDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String sizeGuide;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ProductDetail(Product product, String description, String sizeGuide) {
        this.product = product;
        this.description = description;
        this.sizeGuide = sizeGuide;
    }

    // 설정자 메서드들
    public void setDescription(String description) {
        this.description = description;
    }

    public void setSizeGuide(String sizeGuide) {
        this.sizeGuide = sizeGuide;
    }
}
