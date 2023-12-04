package com.bridgeshop.module.shipment.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.bridgeshop.module.order.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipments")
public class Shipment extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String recipientName;

    @Column(length = 20, nullable = false)
    private String recipientPhone;

    @Column(length = 500, nullable = false)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CourierCompany courierCompany;

    @Column(length = 50)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private com.bridgeshop.module.shipment.entity.ShipmentStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "shipment")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();
}