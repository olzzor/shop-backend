package com.bridgeshop.module.category.entity;

import com.bridgeshop.module.coupon.entity.CouponCategory;
import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.stats.entity.StatsSalesCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<CouponCategory> couponCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<StatsSalesCategory> statsSalesCategories = new ArrayList<>();
}
