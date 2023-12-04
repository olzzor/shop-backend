package com.bridgeshop.module.recentlyviewedproduct.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.module.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "recently_viewed_products")
public class RecentlyViewedProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private LocalDateTime viewedAt;
}

