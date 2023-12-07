package com.bridgeshop.module.coupon.entity;

import com.bridgeshop.module.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}

