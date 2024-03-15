package com.shop.module.coupon.entity;

import com.shop.module.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "coupon_product")
public class CouponProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponProduct(Coupon coupon, Product product) {
        this.coupon = coupon;
        this.product = product;
    }

    // 설정자 메서드들
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

