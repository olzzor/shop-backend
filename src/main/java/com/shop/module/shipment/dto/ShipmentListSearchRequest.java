package com.shop.module.shipment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ShipmentListSearchRequest {
    private String courierCompany;
    private String trackingNumber;
    private String shipmentStatus;
    private String recipientName;
    private String recipientPhone;
    private String orderNumber;
    private String shippingAddress;
    private String orderProduct;
    private String startRegDate;
    private String endRegDate;
    private String startModDate;
    private String endModDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ShipmentListSearchRequest(String courierCompany, String trackingNumber,
                                     String shipmentStatus, String recipientName,
                                     String recipientPhone, String orderNumber,
                                     String shippingAddress, String orderProduct,
                                     String startRegDate, String endRegDate,
                                     String startModDate, String endModDate) {
        this.courierCompany = courierCompany;
        this.trackingNumber = trackingNumber;
        this.shipmentStatus = shipmentStatus;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.orderNumber = orderNumber;
        this.shippingAddress = shippingAddress;
        this.orderProduct = orderProduct;
        this.startRegDate = startRegDate;
        this.endRegDate = endRegDate;
        this.startModDate = startModDate;
        this.endModDate = endModDate;
    }
}
