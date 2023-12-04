package com.bridgeshop.module.cart.entity;

import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.product.entity.ProductSize;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

/** Cart, Product 중간 엔티티 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_product")
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_size_id", referencedColumnName = "id")
    private ProductSize productSize;

    @Column
    private int quantity;

    // 적용 쿠폰
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;
}

