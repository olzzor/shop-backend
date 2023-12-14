package com.bridgeshop.module.order.entity;

import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.shipment.entity.Shipment;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.product.entity.ProductSize;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "order_details")
public class OrderDetail extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "product_size_id", referencedColumnName = "id")
    private ProductSize productSize;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "shipment_id", referencedColumnName = "id")
    private Shipment shipment;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

    @Column(nullable = false)
    private int quantity; // 주문 수량

    @Column(nullable = false)
    private int unitPrice; // 주문 시점 상품 가격

    @Column(nullable = false)
    private int discountPer; // 주문 시점 상품 할인율

    @Column(nullable = false)
    private int finalPrice; // 주문 시점 할인 적용 후 최종 금액

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public OrderDetail(Order order, Product product, ProductSize productSize, Shipment shipment,
                       Coupon coupon, int quantity, int unitPrice, int discountPer, int finalPrice) {
        this.order = order;
        this.product = product;
        this.productSize = productSize;
        this.shipment = shipment;
        this.coupon = coupon;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPer = discountPer;
        this.finalPrice = finalPrice;
    }

    // 설정자 메서드들
    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductSize(ProductSize productSize) {
        this.productSize = productSize;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setDiscountPer(int discountPer) {
        this.discountPer = discountPer;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }
}