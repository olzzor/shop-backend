package com.shop.module.coupon.entity;

import com.shop.module.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "coupon_category")
public class CouponCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponCategory(Coupon coupon, Category category) {
        this.coupon = coupon;
        this.category = category;
    }

    // 설정자 메서드들
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

