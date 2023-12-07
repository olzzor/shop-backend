package com.bridgeshop.module.product.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.cart.entity.CartProduct;
import com.bridgeshop.module.favorite.entity.Favorite;
import com.bridgeshop.module.order.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "product_sizes")
public class ProductSize extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(nullable = false, length = 50)
    private String size;

    @Column(nullable = false)
    private int quantity;

    @OneToMany(mappedBy = "productSize")
    @JsonBackReference
    private List<Favorite> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "productSize")
    @JsonBackReference
    private List<CartProduct> cartProducts = new ArrayList<>();

    // 추가 근데 필요함?
    @OneToMany(mappedBy = "productSize")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
