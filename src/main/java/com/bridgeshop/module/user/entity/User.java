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
@NoArgsConstructor // 매개변수가 없는 기본 생성자를 생성
@AllArgsConstructor // 클래스의 모든 필드를 매개변수로 가지는 생성자를 생성
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider authProvider;

    @Column(length = 100)
    private String socialId;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean adminFlag = false;

    @Column(nullable = false, columnDefinition = "boolean default true")
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
