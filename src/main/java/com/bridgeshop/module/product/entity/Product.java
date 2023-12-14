package com.bridgeshop.module.product.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.cart.entity.CartProduct;
import com.bridgeshop.module.category.entity.Category;
import com.bridgeshop.module.coupon.entity.CouponProduct;
import com.bridgeshop.module.favorite.entity.Favorite;
import com.bridgeshop.module.order.entity.OrderDetail;
import com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(length = 2000)
    private String detail;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int discountPer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

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
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<CouponProduct> couponProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<RecentlyViewedProduct> recentlyViewedProducts = new ArrayList<>();

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Product(Category category, String code, String name, String detail, int price, int discountPer, ProductStatus status) {
        this.category = category;
        this.code = code;
        this.name = name;
        this.detail = detail;
        this.price = price;
        this.discountPer = discountPer;
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

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
