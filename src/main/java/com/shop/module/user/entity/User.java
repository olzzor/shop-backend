package com.shop.module.user.entity;

import com.shop.module.address.entity.Address;
import com.shop.module.cart.entity.Cart;
import com.shop.module.contact.entity.Contact;
import com.shop.module.coupon.entity.CouponUser;
import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.wishlist.entity.Wishlist;
import com.shop.module.order.entity.Order;
import com.shop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import com.shop.module.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
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
    private boolean isAdmin = false;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActivate = true;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Wishlist> wishlist = new ArrayList<>();

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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public User(String email, String password, String name, String phoneNumber,
                AuthProvider authProvider, String socialId, Boolean isAdmin, Boolean isActivate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.authProvider = authProvider;
        this.socialId = socialId;
        this.isAdmin = (isAdmin == null) ? false : isAdmin; // 기본값 설정
        this.isActivate = (isActivate == null) ? true : isActivate; // 기본값 설정
        // 관계형 필드는 생성자에서 초기화하지 않음
    }

    // 설정자 메서드들
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setActivate(boolean isActivate) {
        this.isActivate = isActivate;
    }
}
