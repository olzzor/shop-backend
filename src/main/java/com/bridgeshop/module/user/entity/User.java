package com.bridgeshop.module.user.entity;

import com.bridgeshop.module.address.entity.Address;
import com.bridgeshop.module.cart.entity.Cart;
import com.bridgeshop.module.contact.entity.Contact;
import com.bridgeshop.module.coupon.entity.CouponUser;
import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.favorite.entity.Favorite;
import com.bridgeshop.module.order.entity.Order;
import com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import com.bridgeshop.module.review.dto.Review;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    @Column(columnDefinition = "ENUM('GOOGLE', 'FACEBOOK', 'KAKAO', 'NAVER', 'LOCAL') DEFAULT 'LOCAL'")
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Column(length = 100)
    private String socialId;

    @Column(nullable = false)
    private boolean adminFlag = false;

    @Column(nullable = false)
    private boolean activateFlag = true;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<CouponUser> couponUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<RecentlyViewedProduct> recentlyViewedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Contact> contacts = new ArrayList<>();
}
