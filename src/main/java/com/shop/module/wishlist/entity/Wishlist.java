package com.shop.module.wishlist.entity;

import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.user.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "wishlists")
public class Wishlist extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_size_id", referencedColumnName = "id")
    private ProductSize productSize;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Wishlist(User user, Product product, ProductSize productSize) {
        this.user = user;
        this.product = product;
        this.productSize = productSize;
    }

    // 설정자 메서드들
    public void setUser(User user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }
}
