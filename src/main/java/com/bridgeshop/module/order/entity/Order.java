package com.bridgeshop.module.order.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.review.entity.Review;
import com.bridgeshop.module.user.entity.User;
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
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String orderNumber;

    @Column(nullable = false, length = 50)
    private String buyerEmail;

    @Column(nullable = false, length = 10)
    private String paymentMethod;

    @Column(nullable = false)
    private int paymentAmount;

    @Column(length = 16)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @OneToMany(mappedBy = "order")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Review review;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Order(User user, String orderNumber, String buyerEmail, String paymentMethod,
                 int paymentAmount, String cardNumber, OrderStatus status) {
        this.user = user;
        this.orderNumber = orderNumber;
        this.buyerEmail = buyerEmail;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.cardNumber = cardNumber;
        this.status = status;
        // 관계형 필드는 생성자에서 초기화하지 않음
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}