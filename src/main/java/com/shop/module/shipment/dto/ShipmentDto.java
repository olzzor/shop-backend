package com.shop.module.shipment.dto;

import com.shop.module.order.dto.OrderDetailDto;
import com.shop.module.shipment.entity.CourierCompany;
import com.shop.module.shipment.entity.ShipmentStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ShipmentDto(Long id, String recipientName, String recipientPhone, String shippingAddress,
                       CourierCompany courierCompany, String trackingNumber, ShipmentStatus status,
                       LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.shippingAddress = shippingAddress;
        this.courierCompany = courierCompany;
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    // 설정자 메서드들
    public void setId(Long id) {
        this.id = id;
    }

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

    public void setOrderDetails(List<OrderDetailDto> orderDetailDtoList) {
        this.orderDetails = orderDetailDtoList;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setModDate(LocalDateTime modDate) {
        this.modDate = modDate;
    }
}
