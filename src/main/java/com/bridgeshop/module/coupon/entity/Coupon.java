package com.bridgeshop.module.coupon.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.order.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}

