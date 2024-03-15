package com.shop.module.order.mapper;

import com.shop.module.order.dto.OrderDetailDto;
import com.shop.module.order.entity.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper {

    public OrderDetailDto mapToDto(OrderDetail orderDetail) {

        return OrderDetailDto.builder()
                .id(orderDetail.getId())
                .quantity(orderDetail.getQuantity())
                .unitPrice(orderDetail.getUnitPrice())
                .discountPer(orderDetail.getDiscountPer())  // 추가
                .finalPrice(orderDetail.getFinalPrice())    // 추가
//                .hasReview(orderDetail.getReview() != null)
                .regDate(orderDetail.getRegDate())
                .modDate(orderDetail.getModDate())
                .build();
    }

    public List<OrderDetailDto> mapToDtoList(List<OrderDetail> orderDetailList) {
        return orderDetailList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}