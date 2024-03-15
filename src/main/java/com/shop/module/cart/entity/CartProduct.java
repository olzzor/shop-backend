package com.shop.module.cart.entity;

import com.shop.module.coupon.entity.Coupon;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductSize;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Cart, Product 중간 엔티티 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "cart_product")
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_size_id", referencedColumnName = "id")
    private ProductSize productSize;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon; // 적용 쿠폰

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CartProduct(Cart cart, Product product, ProductSize productSize, int quantity, Coupon coupon) {
        this.cart = cart;
        this.product = product;
        this.productSize = productSize;
        this.quantity = quantity;
        this.coupon = coupon;
    }

    // 설정자 메서드들
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
}

