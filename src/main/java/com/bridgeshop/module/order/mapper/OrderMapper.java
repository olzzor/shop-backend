package com.bridgeshop.module.order.mapper;

import com.bridgeshop.module.order.dto.OrderDto;
import com.bridgeshop.module.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    public OrderDto mapToDto(Order order) {

        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .buyerEmail(order.getBuyerEmail())
                .paymentMethod(order.getPaymentMethod())
                .paymentAmount(order.getPaymentAmount())
                .cardNumber(order.getCardNumber())
                .status(order.getStatus())
                .regDate(order.getRegDate())
                .modDate(order.getModDate())
                .build();
    }

    public List<OrderDto> mapToDtoList(List<Order> orderList) {
        return orderList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}