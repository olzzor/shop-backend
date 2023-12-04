package com.bridgeshop.module.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
