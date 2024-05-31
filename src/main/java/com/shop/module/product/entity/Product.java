package com.shop.module.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.cart.entity.CartProduct;
import com.shop.module.category.entity.Category;
import com.shop.module.contact.entity.Contact;
import com.shop.module.coupon.entity.CouponProduct;
import com.shop.module.order.entity.OrderDetail;
import com.shop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import com.shop.module.recommendedproduct.entity.RecommendedProduct;
import com.shop.module.wishlist.entity.Wishlist;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "products")
public class Product extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "category_id", referencedColumnName = "id")
    private Category category;

    @Column(nullable = false, length = 50, unique = true)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int discountPer;

    @Column(nullable = false)
    private boolean isDisplay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    private ProductDetail productDetail;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<ProductSize> productSizes = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<Wishlist> wishlist = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CouponProduct> couponProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<RecentlyViewedProduct> recentlyViewedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<RecommendedProduct> recommendedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<Contact> contacts = new ArrayList<>(); // 20240521 추가

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Product(Category category, String code, String name, int price, int discountPer, Boolean isDisplay, ProductStatus status) {
        this.category = category;
        this.code = code;
        this.name = name;
        this.price = price;
        this.discountPer = discountPer;
        this.isDisplay = (isDisplay == null) ? true : isDisplay; // 기본값 설정
        this.status = status;
    }

    // 설정자 메서드들
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }

    public void setDisplay(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
