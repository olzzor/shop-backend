package com.shop.module.coupon.entity;

import com.shop.module.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "coupon_user")
public class CouponUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponUser(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
    }

    // 설정자 메서드들
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

