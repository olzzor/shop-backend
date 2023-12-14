package com.bridgeshop.module.recentlyviewedproduct.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.user.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "recently_viewed_products")
public class RecentlyViewedProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @Column(nullable = false)
    private LocalDateTime viewedAt;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public RecentlyViewedProduct(User user, Product product, LocalDateTime viewedAt) {
        this.user = user;
        this.product = product;
        this.viewedAt = viewedAt;
    }

    // 설정자 메서드들
    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }
}

