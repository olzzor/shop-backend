package com.shop.module.category.entity;

import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.coupon.entity.CouponCategory;
import com.shop.module.product.entity.Product;
import com.shop.module.stats.entity.StatsSalesCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "categories")
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(length = 50)
    private String codeRef;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String slug;

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<CouponCategory> couponCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<StatsSalesCategory> statsSalesCategories = new ArrayList<>();

    /** 빌더 패턴을 사용하는 생성자 */
    @Builder
    public Category(String code, String codeRef, String name, String slug) {
        this.code = code;
        this.codeRef = codeRef;
        this.name = name;
        this.slug = slug;
        // 관계형 필드는 생성자에서 초기화하지 않음
    }

    /** 설정자 메서드들 */
    public void setCode(String code) {
        this.code = code;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
