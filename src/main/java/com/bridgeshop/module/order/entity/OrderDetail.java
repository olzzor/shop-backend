package com.bridgeshop.module.order.entity;

import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.shipment.entity.Shipment;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.product.entity.ProductSize;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_details")
public class OrderDetail extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "product_size_id", referencedColumnName = "id")
    private ProductSize productSize;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "shipment_id", referencedColumnName = "id")
    private Shipment shipment;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    // 주문 수량
    @Column(nullable = false)
    private int quantity;

    // 주문 시점 상품 가격
    @Column(nullable = false)
    private int unitPrice;

    // 주문 시점 상품 할인율
    @Column(nullable = false)
    private int discountPer;

    // 주문 시점 할인 적용 후 최종 금액
    @Column(nullable = false)
    private int finalPrice;

//    // OrderDetail 가 아닌 Order 마다 리뷰 작성하도록 변경
//    @OneToOne(mappedBy = "orderDetail", fetch = FetchType.LAZY)
//    private Review review;
}