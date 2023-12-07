package com.bridgeshop.module.order.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.review.dto.Review;
import com.bridgeshop.module.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Builder.Default
    @OneToMany(mappedBy = "order")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Review review;
}