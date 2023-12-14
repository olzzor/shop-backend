package com.bridgeshop.module.coupon.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.order.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "coupons")
public class Coupon extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CouponType type;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 2000)
    private String detail;

    @Column(nullable = false)
    private int minAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CouponDiscountType discountType;

    @Column(nullable = false)
    private int discountValue;

    @Column(nullable = false)
    private LocalDateTime startValidDate;

    @Column(nullable = false)
    private LocalDateTime endValidDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @OneToMany(mappedBy = "coupon")
    @JsonBackReference
    private List<CouponCategory> couponCategories = new ArrayList<>();

    @OneToMany(mappedBy = "coupon")
    @JsonBackReference
    private List<CouponProduct> couponProducts = new ArrayList<>();

    @OneToMany(mappedBy = "coupon")
    @JsonBackReference
    private List<CouponUser> couponUsers = new ArrayList<>();

    @OneToMany(mappedBy = "coupon")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Coupon(CouponType type, String code, String name, String detail,
                  int minAmount, CouponDiscountType discountType, int discountValue,
                  LocalDateTime startValidDate, LocalDateTime endValidDate, CouponStatus status) {
        this.type = type;
        this.code = code;
        this.name = name;
        this.detail = detail;
        this.minAmount = minAmount;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startValidDate = startValidDate;
        this.endValidDate = endValidDate;
        this.status = status;
    }

    // 설정자 메서드들
    public void setType(CouponType type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public void setDiscountType(CouponDiscountType discountType) {
        this.discountType = discountType;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public void setStartValidDate(LocalDateTime startValidDate) {
        this.startValidDate = startValidDate;
    }

    public void setEndValidDate(LocalDateTime endValidDate) {
        this.endValidDate = endValidDate;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }
}

