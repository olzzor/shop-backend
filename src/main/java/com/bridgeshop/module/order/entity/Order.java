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
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50, nullable = false)
    private String orderNumber;

    @Column(length = 50, nullable = false)
    private String buyerEmail;

    @Column(length = 10, nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private int paymentAmount;

    @Column(length = 16)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrderStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "order")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Review review;
}