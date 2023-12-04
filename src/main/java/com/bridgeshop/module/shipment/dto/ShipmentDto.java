package com.bridgeshop.module.shipment.dto;

import com.bridgeshop.module.shipment.entity.CourierCompany;
import com.bridgeshop.module.order.dto.OrderDetailDto;
import com.bridgeshop.module.shipment.entity.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class ShipmentDto {
    private Long id;
    private String recipientName;
    private String recipientPhone;
    private String shippingAddress;
    private CourierCompany courierCompany;
    private String trackingNumber;
    private ShipmentStatus status;
    private List<OrderDetailDto> orderDetails;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
