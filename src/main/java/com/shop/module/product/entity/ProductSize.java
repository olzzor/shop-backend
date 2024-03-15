package com.shop.module.product.entity;

import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.cart.entity.CartProduct;
import com.shop.module.favorite.entity.Favorite;
import com.shop.module.order.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ProductSize(Product product, String size, int quantity) {
        this.product = product;
        this.size = size;
        this.quantity = quantity;
    }

    // 설정자 메서드들
    public void setProduct(Product product) {
        this.product = product;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
