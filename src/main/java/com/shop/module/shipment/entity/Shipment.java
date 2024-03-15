package com.shop.module.shipment.entity;

import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.order.entity.OrderDetail;
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
@Table(name = "shipments")
public class Shipment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String recipientName;

    @Column(nullable = false, length = 20)
    private String recipientPhone;

    @Column(nullable = false, length = 500)
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CourierCompany courierCompany;

    @Column(length = 50)
    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShipmentStatus status;

    @OneToMany(mappedBy = "shipment")
    @JsonBackReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Shipment(String recipientName, String recipientPhone, String shippingAddress,
                    CourierCompany courierCompany, String trackingNumber, ShipmentStatus status) {
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.shippingAddress = shippingAddress;
        this.courierCompany = courierCompany;
        this.trackingNumber = trackingNumber;
        this.status = status;
        // 관계형 필드는 생성자에서 초기화하지 않음
    }

    // 설정자 메서드들
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setCourierCompany(CourierCompany courierCompany) {
        this.courierCompany = courierCompany;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }
}